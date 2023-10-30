import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                displayMenu();
                String option = consoleInput.readLine();
                output.println(option);

                switch (option) {
                    case "1":
                        // Consultar película por ID
                        System.out.print("Ingrese el ID de la película: ");
                        String movieId = consoleInput.readLine();
                        output.println(movieId);
                        break;
                    case "2":
                        // Consultar película por título
                        System.out.print("Ingrese el título de la película: ");
                        String movieTitle = consoleInput.readLine();
                        output.println(movieTitle);
                        break;
                    case "3":
                        // Consultar películas por director
                        System.out.print("Ingrese el nombre del director: ");
                        String director = consoleInput.readLine();
                        output.println(director);
                        break;
                    case "4":
                        // Añadir película
                        Movie newMovie = inputMovieDetails(consoleInput);
                        sendMovieToServer(output, newMovie);
                        break;
                    case "5":
                        // Salir de la aplicación
                        socket.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                        break;
                }

                // Recibir y mostrar la respuesta del servidor
                String response = input.readLine();
                System.out.println("Respuesta del servidor: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("Menú:");
        System.out.println("1. Consultar película por ID");
        System.out.println("2. Consultar película por título");
        System.out.println("3. Consultar películas por director");
        System.out.println("4. Añadir nueva película");
        System.out.println("5. Salir de la aplicación");
    }

    // Método para recibir los detalles de la película desde el usuario
    private static Movie inputMovieDetails(BufferedReader consoleInput) throws IOException {
        System.out.print("Introduzca el ID de la película: ");
        int id = Integer.parseInt(consoleInput.readLine());

        System.out.print("Introduzca el título de la película: ");
        String title = consoleInput.readLine();

        System.out.print("Introduzca el nombre del director: ");
        String director = consoleInput.readLine();

        System.out.print("Introduzca el precio de la película: ");
        double price = Double.parseDouble(consoleInput.readLine());

        return new Movie(id, title, director, price);
    }

    private static void sendMovieToServer(PrintWriter output, Movie movie) {
        output.println(movie.getId());
        output.println(movie.getTitle());
        output.println(movie.getDirector());
        output.println(movie.getPrice());
    }
}