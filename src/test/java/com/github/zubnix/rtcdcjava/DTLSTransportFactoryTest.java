package com.github.zubnix.rtcdcjava;


import org.bouncycastle.crypto.tls.ByteQueueOutputStream;
import org.bouncycastle.crypto.tls.DTLSTransport;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DTLSTransportFactoryTest {

    @Test
    public void testClientServer() throws IOException, InterruptedException {
        //given
        //client & server connection


        final DTLSTransportFactory dtlsTransportFactory = new DTLSTransportFactory();


        //when
        //a message is send from client to server & vice verse

        //server thread
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
                                                                                                             return datagramPacket.getLength();
                                                                                                         }
                                                                                                         catch (final SocketTimeoutException e) {
                                                                                                             return 0;
                                                                                                         }
                                                                                                     }

                                                                                                     @Override
                                                                                                     public void send(final byte[] buf,
                                                                                                                      final int off,
                                                                                                                      final int len) throws IOException {
                                                                                                         final InetAddress IPAddress = InetAddress.getByName("localhost");
                                                                                                         serverSocket.send(new DatagramPacket(buf,
                                                                                                                                              len,
                                                                                                                                              IPAddress,
                                                                                                                                              9876));
                                                                                                     }

                                                                                                     @Override
                                                                                                     public void close() throws IOException {
                                                                                                         serverSocket.close();
                                                                                                     }
                                                                                                 });

                final byte[] receiveBuffer = new byte[3];
                serverTransport.receive(receiveBuffer,
                                        0,
                                        3,
                                        100000);

                Assert.assertEquals("SYN",
                                    new String(receiveBuffer));

                final byte[] message = "ACK".getBytes();
                serverTransport.send(message,
                                     0,
                                     message.length);
            }
            catch (final IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }
        });
        serverThread.start();

        final Thread clientThread = new Thread(() -> {
            try {
                //client connection
                final DatagramSocket clientSocket = new DatagramSocket();

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
                            return 0;
                        }
                    }

                    @Override
                    public void send(final byte[] buf,
                                     final int off,
                                     final int len) throws IOException {
                        final InetAddress IPAddress = InetAddress.getByName("localhost");
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

                final byte[] message = "SYN".getBytes();
                clientTransport.send(message,
                                     0,
                                     message.length);

                final byte[] receiveBuffer = new byte[10];
                clientTransport.receive(receiveBuffer,
                                        0,
                                        3,
                                        100000);
                Assert.assertEquals("ACK",
                                    new String(receiveBuffer));

            }
            catch (final IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }
        });
        clientThread.start();

        //then
        //the message is correctly encrypted and decrypted
        clientThread.join();
        serverThread.join();

    }
}