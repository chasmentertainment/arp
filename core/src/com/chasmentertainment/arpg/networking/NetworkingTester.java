package com.chasmentertainment.arpg.networking;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.*;
import org.zeromq.ZMsg;
import sun.security.ssl.Debug;
import zmq.Rep;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Tyler on 6/7/2014.
 */
public class NetworkingTester {
    private static Random rand = new Random(System.nanoTime());
                                                                                // Request - Reply
    public static void testReqRep() throws Exception {

        final ZContext ctx = new ZContext();
        // Server
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Socket replySocket = ctx.createSocket(ZMQ.REP);
                replySocket.bind("tcp://127.0.0.1:5678");
                while (!Thread.currentThread().isInterrupted()) {
                    String message = replySocket.recvStr();

                    replySocket.send("Echo: " + message);

                    Debug.println("Debug", message);
                }
                ctx.destroy();
            }
        }).start();

        // Client
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Socket requestSocket = ctx.createSocket(ZMQ.REQ);
                requestSocket.connect("tcp://127.0.0.1:5678");

                requestSocket.send("Hello");

                Debug.println("Debug", requestSocket.recvStr());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                ZContext ctx = new ZContext();

                final Socket requestSocket = ctx.createSocket(ZMQ.REQ);
                requestSocket.connect("tcp://127.0.0.1:5678");

                requestSocket.send("World");

                Debug.println("Debug", requestSocket.recvStr());
            }
        }).start();
    }
                                                                                // Publisher - Subscriber
    public static void testPubSub() throws Exception {
        Debug.println("Debug", "Running Publisher Subscriber Test");
        final ZContext ctx = new ZContext();

        final Socket publisherSocket = ctx.createSocket(ZMQ.PUB);
        publisherSocket.bind("tcp://127.0.0.1:5680");

        // Publisher
        new Thread(new Runnable() {
            Calendar calendar = Calendar.getInstance();

            private void loop(int id) {
                try {
                    Thread.sleep(1000);

                    calendar.setTimeInMillis(System.currentTimeMillis());

                    publisherSocket.send(String.format("1-Update! >> %d >> %s", id, calendar.getTime()));
                    publisherSocket.send(String.format("2-Update! >> %d >> %s", id, calendar.getTime()));
                    publisherSocket.send(String.format("3-Update! >> %d >> %s", id, calendar.getTime()));
                    publisherSocket.send(String.format("4-Update! >> %d >> %s", id, calendar.getTime()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!Thread.currentThread().isInterrupted())
                    loop(id+1);
            }

            @Override
            public void run() {
                loop(0);
                ctx.destroy();
            }
        }).start();

        // Subscriber
        new Thread(new Runnable() {
            Socket subscriberSocket;

            private void loop() {
                String message = subscriberSocket.recvStr();
                Debug.println("Debug", message);

                if (!Thread.currentThread().isInterrupted())
                    loop();
            }

            @Override
            public void run() {
                subscriberSocket = ctx.createSocket(ZMQ.SUB);
                subscriberSocket.subscribe("2".getBytes());
                subscriberSocket.connect("tcp://127.0.0.1:5680");

                loop();
            }
        }).start();
    }
                                                                                // Pipeline (Push/Pull)
    public static void testPipeline() throws Exception {
        final ZContext ctx = new ZContext();

        // Manager
        new Thread(new Runnable() {
            Socket mManagerSocket;
            Calendar mCalendar = Calendar.getInstance();

            @Override
            public void run() {
                mManagerSocket = ctx.createSocket(ZMQ.PUSH);

                mManagerSocket.bind("tcp://127.0.0.1:5690");

                int id =0;
                while (!Thread.currentThread().isInterrupted()) {
                    mCalendar.setTimeInMillis(System.currentTimeMillis());

                    String msg = String.format("%d - %s", id++,
                            mCalendar.getTime());
                    mManagerSocket.send(msg);
                    Debug.println("Debug", "Sent: " + msg);
                }
                ctx.destroy();
            }
        }).start();

        // Worker
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZContext ctx = new ZContext();
                Socket workerSocket = ctx.createSocket(ZMQ.PULL);
                workerSocket.connect("tcp://127.0.0.1:5690");

                while (!Thread.currentThread().isInterrupted()) {
                    String message = workerSocket.recvStr();
                    Debug.println("Debug", "Received: " + message);
                }

            }
        }).start();
    }
                                                                                // Exclusive Pair
    public static void testExclusivePair() throws Exception {
        final ZContext ctx = new ZContext();

        // Bind
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket bindSocket = ctx.createSocket(ZMQ.PAIR);

                bindSocket.bind("tcp://127.0.0.1:5696");
                int id=0;
                while (id < 500 && !Thread.currentThread().isInterrupted()) {
                    bindSocket.send("turn down for what: " + id++);
                }
                ctx.destroy();
            }
        }).start();

        // Connect
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket connectSocket = ctx.createSocket(ZMQ.PAIR);
                connectSocket.connect("tcp://127.0.0.1:5696");

                while (!Thread.currentThread().isInterrupted()) {
                    String message = connectSocket.recvStr();
                    Debug.println("Debug", "Received: " + message);
                }
            }
        }).start();
    }
                                                                                // Proxy
    public static void testProxy() throws Exception {
        ZContext ctx = new ZContext();

        new Thread(new ClientTask()).start();
        new Thread(new ClientTask()).start();
        new Thread(new ClientTask()).start();
        new Thread(new ServerTask()).start();

        Thread.sleep(5*1000);
        ctx.destroy();
    }

    private static class ClientTask implements Runnable {

        @Override
        public void run() {
            /*
                This method is being run in its own thread.
                It will contain all of the logic for the Client:

                1. socket creation
                2. assign own identity randomly
                3. connect to the remote server (localhost in this case)
                4. poll for new requests every 100th of a second.
             */

            // Create a new context to contain state for connections
            ZContext ctx = new ZContext();

            // create a socket inside of the newly created context
            Socket client = ctx.createSocket(ZMQ.DEALER);

            String identity = String.format("%04X-%04X", rand.nextInt(),
                    rand.nextInt());

            client.setIdentity(identity.getBytes());

            // connect to the host
            client.connect("tcp://localhost:5570");

            // create container for received items
            PollItem[] items = new PollItem[] {
                new PollItem(client, Poller.POLLIN)
            };

            int requestNbr = 0;

            // poll for new messages.
            // if any are found, print the message prefixed by the identity
            // then send a response and begin polling again
            // Once the thread is stopped, destroy the ZMQ context
            while (!Thread.currentThread().isInterrupted()) {
                for (int centitick=0; centitick<100; centitick++) {
                    ZMQ.poll(items, 10);
                    if (items[0].isReadable()) {
                        ZMsg msg = ZMsg.recvMsg(client);
                        msg.getLast().print(identity);
                        msg.destroy();
                    }
                }
                client.send(String.format("request #%d", ++requestNbr), 0);
            }
            ctx.destroy();
        }

    }

    private static class ServerTask implements Runnable {

        @Override
        public void run() {
            // Create a new context for the server task to run in
            ZContext ctx = new ZContext();

            // BIND the server's socket to capture messages from
            // all IP addresses with port 5570
            Socket frontend = ctx.createSocket(ZMQ.ROUTER);
            frontend.bind("tcp://*:5570");

            // BIND the server's backend socket to filter messages
            // through memory "inproc" to the backend socket
            Socket backend = ctx.createSocket(ZMQ.DEALER);
            backend.bind("inproc://backend");

            // Create new worker threads for this server task and start them.
            for (int threadNbr=0; threadNbr<5; threadNbr++) {
                new Thread(new ServerWorker(ctx)).start();
            }

            // filter messages from the frontend to the backend end?
            ZMQ.proxy(frontend, backend, null);

            ctx.destroy();
        }
    }

    private static class ServerWorker implements Runnable {
        private ZContext ctx;

        public ServerWorker(ZContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            Socket worker = ctx.createSocket(ZMQ.DEALER);
            worker.connect("inproc://backend");

            while (!Thread.currentThread().isInterrupted()) {
                ZMsg msg = ZMsg.recvMsg(worker);
                ZFrame address = msg.pop();
                ZFrame content = msg.pop();

                assert (content != null);

                msg.destroy();

                int replies = rand.nextInt(5);

                for (int reply = 0; reply < replies; reply++) {
                    try {
                        Thread.sleep(rand.nextInt(1000)+ 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // TODO: What is REUSE and MORE?
                    address.send(worker, ZFrame.REUSE + ZFrame.MORE);

                    content.send(worker, ZFrame.REUSE);
                }
            }
        }
    }
}
