import java.time.LocalDate;
import java.util.*;

class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable = true;
    private LocalDate dueDate = null;

    public Book(int id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @Override
    public String toString() {
        return "Book ID: " + id + ", Title: " + title + ", Author: " + author + ", Category: " + category + ", Available: " + isAvailable;
    }
}

class User {
    private String username;
    private String role; // "admin" or "member"
    private List<Book> borrowedBooks = new ArrayList<>();

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
}

class LibraryManagementSystem {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private User loggedInUser = null;

    public void addBook(Book book) {
        books.put(book.getId(), book);
        System.out.println("Book added: " + book);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        System.out.println("User added: " + user.getUsername());
    }

    public void login(String username) {
        if (users.containsKey(username)) {
            loggedInUser = users.get(username);
            System.out.println("Logged in as: " + username);
        } else {
            System.out.println("User not found!");
        }
    }

    public void borrowBook(int bookId) {
        if (loggedInUser == null || !loggedInUser.getRole().equals("member")) {
            System.out.println("Only members can borrow books!");
            return;
        }
        Book book = books.get(bookId);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            book.setDueDate(LocalDate.now().plusDays(14)); // Due in 2 weeks
            loggedInUser.getBorrowedBooks().add(book);
            System.out.println("Book borrowed: " + book.getTitle());
        } else {
            System.out.println("Book not available!");
        }
    }

    public void returnBook(int bookId) {
        if (loggedInUser == null || !loggedInUser.getRole().equals("member")) {
            System.out.println("Only members can return books!");
            return;
        }
        Book book = books.get(bookId);
        if (book != null && loggedInUser.getBorrowedBooks().contains(book)) {
            book.setAvailable(true);
            loggedInUser.getBorrowedBooks().remove(book);
            LocalDate now = LocalDate.now();
            if (now.isAfter(book.getDueDate())) {
                System.out.println("Book returned late! Fine: $" + (now.toEpochDay() - book.getDueDate().toEpochDay()));
            } else {
                System.out.println("Book returned on time.");
            }
        } else {
            System.out.println("Book not found in borrowed list!");
        }
    }

    public void listBooks() {
        System.out.println("Books in the library:");
        books.values().forEach(System.out::println);
    }

    public void listBorrowedBooks() {
        if (loggedInUser == null || !loggedInUser.getRole().equals("member")) {
            System.out.println("Only members can view borrowed books!");
            return;
        }
        System.out.println("Borrowed books:");
        loggedInUser.getBorrowedBooks().forEach(System.out::println);
    }
}

public class LibraryApp {
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();

        // Adding books
        library.addBook(new Book(1, "The Alchemist", "Paulo Coelho", "Fiction"));
        library.addBook(new Book(2, "Clean Code", "Robert C. Martin", "Programming"));
        library.addBook(new Book(3, "1984", "George Orwell", "Dystopian"));

        // Adding users
        library.addUser(new User("admin", "admin"));
        library.addUser(new User("john", "member"));

        // User actions
        library.login("john");
        library.listBooks();
        library.borrowBook(1);
        library.borrowBook(2);
        library.listBorrowedBooks();
        library.returnBook(1);
        library.listBorrowedBooks();
    }
}
