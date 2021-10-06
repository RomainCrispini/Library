package fr.romain.library.controller;

import fr.romain.library.entity.Book;
import fr.romain.library.utils.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Button btnInsert, btnUpdate, btnDelete;
    @FXML private TextField tfId, tfTitle, tfAuthor, tfYear, tfPages;
    @FXML private TableView<Book> tvBooks;
    @FXML private TableColumn<Book, Integer> colId, colYear, colPages;
    @FXML private TableColumn<Book, String> colTitle, colAuthor;

    @FXML
    void handleButtonAction(ActionEvent event) throws SQLException {
        if (event.getSource() == btnInsert) {
            insertBook();
        } else if (event.getSource() == btnUpdate) {
            updateBook();
        } else if (event.getSource() == btnDelete) {
            deleteBook();
        }
    }

    @FXML
    void handleMouseAction() {
        // Permet de récupérer les propriétés de l'item cliqué dans le TazbleView
        Book book = tvBooks.getSelectionModel().getSelectedItem();
        // On lie au clic l'objet book avec les TextField
        tfId.setText(String.valueOf(book.getId()));
        tfTitle.setText(book.getTitle());
        tfAuthor.setText(book.getAuthor());
        tfYear.setText(String.valueOf(book.getYear()));
        tfPages.setText(String.valueOf(book.getPages()));
    }

    private ObservableList<Book> getBookList() {
        ObservableList<Book> booksList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        String query = "SELECT * FROM book";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"),
                        rs.getString("author"), rs.getInt("year"),
                        rs.getInt("pages"));
                booksList.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur dans le SELECT *");
        }
        return booksList;
    }

    private void displayTableView() {
        ObservableList<Book> listBooks = getBookList();
        // On fait correspondre chacune des propriétés de l'objet book avec chacune des colonnes du TableView
        colId.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
        tvBooks.setItems(listBooks);
    }

    private void insertBook() throws SQLException {
        String query = "INSERT INTO book VALUES ('" + tfId.getText() + "', '" + tfTitle.getText() + "', '" +
                tfAuthor.getText() + "', '" + tfYear.getText() + "', '" + tfPages.getText() + "');";
        executeQuery(query);
        displayTableView();
    }

    private void deleteBook() throws SQLException {
        String query = "DELETE FROM book WHERE id = '" + tfId.getText() + "'";
        executeQuery(query);
        displayTableView();
    }

    private void updateBook() throws SQLException {
        String query = "UPDATE book SET title = '" + tfTitle.getText() + "', author = '" + tfAuthor.getText() +
                "', year = '" + tfYear.getText() + "', pages = '" + tfPages.getText() + "' WHERE id = '" +
                tfId.getText() + "';";
        executeQuery(query);
        displayTableView();
    }

    // Méthode qui permet de factoriser la connection à la BDD, en même temps que d'exécuter nos requêtes
    private void executeQuery(String query) throws SQLException {
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur SQL");
        }
        connection.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTableView();
    }
}
