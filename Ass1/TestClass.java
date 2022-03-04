import java.io.*;
import java.util.*;

class Book<T, E> {
    private T bookTitle;
    private E bookEdition;

    public Book(T bookTitle, E bookEdition) {
        this.bookTitle = bookTitle;
        this.bookEdition = bookEdition;
    }

    public T getBookTitle() {
        return bookTitle;
    }

    public String toString() {
        return String.valueOf(this.bookTitle) + " --> " + String.valueOf(this.bookEdition);
    }

}

class Library<T, E> {
    ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public boolean addBook(T bookTitle, E bookEdition) {
        Book book = new Book(bookTitle, bookEdition);

        if (!books.contains(book)) {
            books.add(book);
            return true;
        }

        return false;
    }

    public void bookList() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

}

public class TestClass{

    public static void main(String[] args){

        Library<String, Integer> lib1 = new Library<String, Integer>();
        lib1.addBook("Introduction to Java", 10);
        lib1.addBook("Java Programming", 5);
        lib1.addBook("Introduction to Java", 10);
        lib1.addBook("Introduction to Java", 3);

        System.out.println("Library 1\n==================");
        lib1.bookList();


        Library<String, String> lib2 = new Library<String, String>();
        lib2.addBook("Teach yourself OOP", "3rd edition");
        lib2.addBook("OOP for dummies", "5th edition");
        lib2.addBook("Teach yourself OOP", "10th edition");
        lib2.addBook("Teach yourself oop", "3rd edition");

        System.out.println("\nLibrary 2\n==================");
        lib2.bookList();
    }
}