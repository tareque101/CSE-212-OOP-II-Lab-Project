import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

class Expense {
    private String category;
    private double amount;
    private String description;

    public Expense(String category, double amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDetails() {
        return category + " | $" + amount + " | " + description;
    }
}

public class ExpenseTracker extends Application {
    private ObservableList<Expense> expenses = FXCollections.observableArrayList();
    private ListView<String> expenseListView = new ListView<>();
    private Label totalLabel = new Label("Total: $0.00");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter Category");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter Description");
        Button addButton = new Button("Add Expense");

        addButton.setOnAction(e -> {
            try {
                String category = categoryField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();

                if (!category.isEmpty() && amount > 0) {
                    Expense expense = new Expense(category, amount, description);
                    expenses.add(expense);
                    expenseListView.getItems().add(expense.getDetails());
                    updateTotal();
                    categoryField.clear();
                    amountField.clear();
                    descriptionField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid amount. Please enter a valid number.");
            }
        });

        Button removeButton = new Button("Remove Expense");
        removeButton.setOnAction(e -> {
            int selectedIdx = expenseListView.getSelectionModel().getSelectedIndex();
            if (selectedIdx >= 0) {
                expenses.remove(selectedIdx);
                expenseListView.getItems().remove(selectedIdx);
                updateTotal();
            }
        });

        root.getChildren().addAll(categoryField, amountField, descriptionField, addButton, expenseListView, removeButton, totalLabel);

        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }

    private void updateTotal() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
