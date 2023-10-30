import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<Movie> movies;
    private PrintWriter output;

    public ClientHandler(Socket clientSocket, List<Movie> movies) {
        this.clientSocket = clientSocket;
        this.movies = movies;
        try {
            this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                String option = input.readLine();

                switch (option) {
                    case "1":
                        // Consultar película por ID
                        int movieId = Integer.parseInt(input.readLine());
                        Movie movieById = findMovieById(movieId);
                        output.println(movieById.getTitle());
                        break;
                    case "2":
                        // Consultar película por título
                        String movieTitle = input.readLine();
                        Movie movieByTitle = findMovieByTitle(movieTitle);
                        output.println(movieByTitle.getTitle());
                        break;
                    case "3":
                        // Consultar películas por director
                        String director = input.readLine();
                        List<Movie> moviesByDirector = findMoviesByDirector(director);
                        sendMovieList(output, moviesByDirector);
                        break;
                    case "4":
                        // Añadir película
                        Movie newMovie = receiveMovieFromClient(input);
                        addMovie(newMovie);
                        output.println("Película añadida con éxito");
                        break;
                    case "5":
                        // Salir de la aplicación
                        clientSocket.close();
                        return;
                    default:
                        output.println("Opción no válida");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Movie findMovieById(int id) {
        return movies.stream().filter(movie -> movie.getId() == id).findFirst().orElse(null);
    }

    private Movie findMovieByTitle(String title) {
        return movies.stream().filter(movie -> movie.getTitle().equals(title)).findFirst().orElse(null);
    }

    private List<Movie> findMoviesByDirector(String director) {
        return movies.stream().filter(movie -> movie.getDirector().equals(director)).collect(Collectors.toList());
    }

    private void sendMovieList(PrintWriter output, List<Movie> movies) {
        Gson gson = new Gson();
        String moviesJson = gson.toJson(movies);
        output.println(moviesJson);
    }

    private Movie receiveMovieFromClient(BufferedReader input) throws IOException {
        int id = Integer.parseInt(input.readLine());
        String title = input.readLine();
        String director = input.readLine();
        double price = Double.parseDouble(input.readLine());

        return new Movie(id, title, director, price);
    }

    private void addMovie(Movie movie) {
        if (!movieExists(movie.getId())) {
            Server.addMovie(movie);
            output.println("Película añadida con éxito");
        } else {
            output.println("Error: Ya existe una película con ese ID.");
        }
    }

    private boolean movieExists(int id) {
        return movies.stream().anyMatch(movie -> movie.getId() == id);
    }
}