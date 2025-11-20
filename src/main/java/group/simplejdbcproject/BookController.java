package group.simplejdbcproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/books")
public class BookController {

	@GetMapping("getbooks")
    public List<Book> getBooks() throws Exception {
        List<Book> list = new ArrayList<>();
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb", "root", "root");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM book");
        while (rs.next()) {
            list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author")));
        }
        con.close();
        return list;
    }
	
	@PostMapping("addbooks")
    public String addBook(@RequestBody Book b) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb", "root", "root");
        PreparedStatement ps = con.prepareStatement("INSERT INTO book (title, author) VALUES (?, ?)");
        ps.setString(1, b.getTitle());
        ps.setString(2, b.getAuthor());
        ps.executeUpdate();
        con.close();
        return "✅ Book added successfully!";
    }

   
    @PutMapping("updatebooks")
    public String updateBook(@RequestBody Book b) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb", "root", "root");
        PreparedStatement ps = con.prepareStatement("UPDATE book SET title=?, author=? WHERE id=?");
        ps.setString(1, b.getTitle());
        ps.setString(2, b.getAuthor());
        ps.setInt(3, b.getId());
        int rows = ps.executeUpdate();
        con.close();
        return rows > 0 ? "✅ Book updated successfully!" : "⚠️ Book not found!";
    }

    
    @DeleteMapping("deletebook/{id}")
    public String deleteBook(@PathVariable int id) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb", "root", "root");
        PreparedStatement ps = con.prepareStatement("DELETE FROM book WHERE id=?");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        con.close();
        return rows > 0 ? "✅ Book deleted successfully!" : "⚠️ Book not found!";
    }

	
}
