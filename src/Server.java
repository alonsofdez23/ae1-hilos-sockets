import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static List<Movie> movies;
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        movies = createMovieList();

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor escuchando en el puerto 12345...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, movies);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Movie> createMovieList() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(1, "El padrino", "Francis Ford Coppola", 18.75));
        movies.add(new Movie(2, "Cadena perpetua", "Frank Darabont", 14.50));
        movies.add(new Movie(3, "El club de la lucha", "David Fincher", 12.35));
        movies.add(new Movie(4, "Scarface", "Brian De Palma", 13.50));
        movies.add(new Movie(5, "La red social", "David Fincher", 15.45));
        return movies;
    }

    public static void addMovie(Movie movie) {
        lock.lock();
        try {
            if (!movieExists(movie.getId())) {
                movies.add(movie);
            } else {
                System.out.println("Error: Ya existe una película con ese ID.");
            }
        } finally {
            lock.unlock();
        }
    }

    private static boolean movieExists(int id) {
        return movies.stream().anyMatch(movie -> movie.getId() == id);
    }
}