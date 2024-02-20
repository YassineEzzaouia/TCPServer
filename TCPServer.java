import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class TCPServer {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client " + clientSocket + ": " + inputLine);
                    // Simulate processing time
                    Thread.sleep(1000);
                    // Reverse the string
                    String reversed = new StringBuilder(inputLine).reverse().toString();
                    out.println(reversed);
                    System.out.println("Sent to client " + clientSocket + ": " + reversed);
                }

                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
