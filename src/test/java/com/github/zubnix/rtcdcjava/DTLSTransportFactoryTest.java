package com.github.zubnix.rtcdcjava;


import org.bouncycastle.crypto.tls.DTLSTransport;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DTLSTransportFactoryTest {

    @Test
    public void testClientServer() throws IOException, InterruptedException, ExecutionException {
        //given
        //client & server connection


        final DTLSTransportFactory dtlsTransportFactory = new DTLSTransportFactory();


        //when
        //a message is send from client to server & vice verse

        //server thread
        final CompletableFuture<Void> serverCompletionState = new CompletableFuture<>();
        final Thread serverThread = new Thread(() -> {
            try {
                //server connection
                final DatagramSocket serverSocket = new DatagramSocket(9876);

                final DTLSTransport serverTransport = dtlsTransportFactory.createServerTransport(RTCCertificate.generate("test"),
                                                                                                 new DatagramTransport() {
                                                                                                     @Override
                                                                                                     public int getReceiveLimit() throws IOException {
                                                                                                         return 1500;
                                                                                                     }

                                                                                                     @Override
                                                                                                     public int getSendLimit() throws IOException {
                                                                                                         return 1500;
                                                                                                     }

                                                                                                     @Override
                                                                                                     public int receive(final byte[] buf,
                                                                                                                        final int off,
                                                                                                                        final int len,
                                                                                                                        final int waitMillis) throws IOException {
                                                                                                         final DatagramPacket datagramPacket = new DatagramPacket(buf,
                                                                                                                                                                  len);
                                                                                                         serverSocket.setSoTimeout(waitMillis);
                                                                                                         try {
                                                                                                             serverSocket.receive(datagramPacket);
                                                                                                             System.out.println(datagramPacket.getLength());
                                                                                                             return datagramPacket.getLength();
                                                                                                         }
                                                                                                         catch (final SocketTimeoutException e) {
                                                                                                             return -1;
                                                                                                         }
                                                                                                     }

                                                                                                     @Override
                                                                                                     public void send(final byte[] buf,
                                                                                                                      final int off,
                                                                                                                      final int len) throws IOException {
                                                                                                         final InetAddress IPAddress = InetAddress.getLoopbackAddress();
                                                                                                         serverSocket.send(new DatagramPacket(buf,
                                                                                                                                              len,
                                                                                                                                              IPAddress,
                                                                                                                                              12345));
                                                                                                     }

                                                                                                     @Override
                                                                                                     public void close() throws IOException {
                                                                                                         serverSocket.close();
                                                                                                     }
                                                                                                 });

                final byte[] receiveBuffer = new byte[1500];
                int          len           = -1;

                int       retry    = 0;
                final int maxRetry = 5;
                //keep trying to receive a SYN
                while ((len = serverTransport.receive(receiveBuffer,
                                                      0,
                                                      1500,
                                                      500)) == -1 && retry < maxRetry) {
                    retry++;
                }

                Assert.assertEquals("SYN",
                                    new String(receiveBuffer,
                                               0,
                                               len));

                //so we can send an ACK
                final byte[] message = "ACK".getBytes();
                serverTransport.send(message,
                                     0,
                                     message.length);
            }
            catch (final IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }

            serverCompletionState.complete(null);
        });
        serverThread.setUncaughtExceptionHandler((t, e) -> serverCompletionState.completeExceptionally(e));
        serverThread.start();

        final CompletableFuture<Void> clientCompletionState = new CompletableFuture<>();
        final Thread clientThread = new Thread(() -> {
            try {
                //client connection
                final DatagramSocket clientSocket = new DatagramSocket(12345);

                final DTLSTransport clientTransport = dtlsTransportFactory.createClientTransport(new DatagramTransport() {
                    @Override
                    public int getReceiveLimit() throws IOException {
                        return 1500;
                    }

                    @Override
                    public int getSendLimit() throws IOException {
                        return 1500;
                    }

                    @Override
                    public int receive(final byte[] buf,
                                       final int off,
                                       final int len,
                                       final int waitMillis) throws IOException {
                        final DatagramPacket datagramPacket = new DatagramPacket(buf,
                                                                                 len);
                        clientSocket.setSoTimeout(waitMillis);
                        try {
                            clientSocket.receive(datagramPacket);
                            return datagramPacket.getLength();
                        }
                        catch (final SocketTimeoutException e) {
                            return -1;
                        }
                    }

                    @Override
                    public void send(final byte[] buf,
                                     final int off,
                                     final int len) throws IOException {
                        final InetAddress IPAddress = InetAddress.getLoopbackAddress();
                        clientSocket.send(new DatagramPacket(buf,
                                                             len,
                                                             IPAddress,
                                                             9876));
                    }

                    @Override
                    public void close() throws IOException {
                        clientSocket.close();
                    }
                });


                //keep sending SYN
                int       retry    = 0;
                final int maxRetry = 5;
                do {
                    retry++;
                    final byte[] message = "SYN".getBytes();
                    clientTransport.send(message,
                                         0,
                                         message.length);
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //until we receive an ACK
                } while (!receiveACK(clientTransport) && retry <= maxRetry);


            }
            catch (final IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }

            clientCompletionState.complete(null);
        });

        clientThread.setUncaughtExceptionHandler((t, e) -> clientCompletionState.completeExceptionally(e));
        clientThread.start();

        //then
        //the message is correctly encrypted and decrypted
        clientThread.join();
        serverThread.join();

        clientCompletionState.get();
        serverCompletionState.get();
    }

    private boolean receiveACK(final DTLSTransport clientTransport) throws IOException {

        final byte[] receiveBuffer = new byte[1500];

        final int len;
        if ((len = clientTransport.receive(receiveBuffer,
                                           0,
                                           1500,
                                           1000)) != -1) {
            Assert.assertEquals("ACK",
                                new String(receiveBuffer,
                                           0,
                                           len));

            return true;
        }
        else {
            return false;
        }
    }
}