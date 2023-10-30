import com.google.gson.Gson;

public class Movie {
    private int id;
    private String title;
    private String director;
    private double price;

    public Movie(int id, String title, String director, double price) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public double getPrice() {
        return price;
    }

    // Método para convertir la película a formato JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}