package ru.fa.carrental.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fa.carrental.client.api.CarApi;
import ru.fa.carrental.client.model.Car;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Главный класс JavaFX клиента автопроката.
 *
 * <p>Обеспечивает CRUD-интерфейс для работы с автомобилями через REST API.</p>
 */
public class MainApp extends Application {

    private static final String PREFS_NODE = "ru.fa.carrental.client";
    private static final String PREF_WIDTH = "window.width";
    private static final String PREF_HEIGHT = "window.height";
    private static final String PREF_X = "window.x";
    private static final String PREF_Y = "window.y";

    private final CarApi carApi = new CarApi("http://localhost:8080/api/cars");
    private final TableView<Car> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Автопрокат - Клиент");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));

        root.setTop(new VBox(createMenuBar(), createToolbar()));
        root.setCenter(createTable());
        root.setBottom(createStatusBar());

        restoreWindowState(primaryStage);

        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> saveWindowState(primaryStage));

        refreshTable();
    }

    private ToolBar createToolbar() {
        Button refresh = new Button("Обновить");
        refresh.setOnAction(e -> refreshTable());

        Button add = new Button("Добавить");
        add.setOnAction(e -> new CarFormDialog(null, carApi, this::refreshTable).showAndWait());

        Button edit = new Button("Редактировать");
        edit.setOnAction(e -> {
            Car selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.INFORMATION, "Выберите запись", "Пожалуйста, выберите автомобиль для редактирования.");
                return;
            }
            new CarFormDialog(selected, carApi, this::refreshTable).showAndWait();
        });

        Button delete = new Button("Удалить");
        delete.setOnAction(e -> {
            Car selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.INFORMATION, "Выберите запись", "Пожалуйста, выберите автомобиль для удаления.");
                return;
            }
            if (confirm("Удалить автомобиль", "Вы уверены, что хотите удалить выбранный автомобиль?")) {
                carApi.deleteCar(selected.getId());
                refreshTable();
            }
        });

        TextField filterField = new TextField();
        filterField.setPromptText("Фильтр по модели...");
        filterField.setOnAction(e -> refreshTable(filterField.getText()));

        return new ToolBar(refresh, add, edit, delete, new Separator(), new Label("Поиск:"), filterField);
    }

    private TableView<Car> createTable() {
        TableColumn<Car, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());

        TableColumn<Car, String> modelCol = new TableColumn<>("Модель");
        modelCol.setCellValueFactory(cell -> cell.getValue().modelProperty());

        TableColumn<Car, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(cell -> cell.getValue().typeProperty());

        TableColumn<Car, Integer> yearCol = new TableColumn<>("Год");
        yearCol.setCellValueFactory(cell -> cell.getValue().yearProperty().asObject());

        TableColumn<Car, Double> priceCol = new TableColumn<>("Цена/день");
        priceCol.setCellValueFactory(cell -> cell.getValue().pricePerDayProperty().asObject());

        TableColumn<Car, Boolean> availableCol = new TableColumn<>("Доступен");
        availableCol.setCellValueFactory(cell -> cell.getValue().availableProperty());

        table.getColumns().addAll(idCol, modelCol, typeCol, yearCol, priceCol, availableCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private VBox createStatusBar() {
        Label stats = new Label("Статистика загружается...");

        Button statsBtn = new Button("Обновить статистику");
        statsBtn.setOnAction(e -> {
            CarApi.Stats stat = carApi.getStats();
            if (stat != null) {
                stats.setText(String.format("Всего: %d, Ср. цена: %.2f, Мин: %.2f, Макс: %.2f",
                        stat.count(), stat.avgPrice(), stat.minPrice(), stat.maxPrice()));
            }
        });

        HBox box = new HBox(10, statsBtn, stats);
        box.setPadding(new Insets(10, 0, 0, 0));
        return new VBox(box);
    }

    private void refreshTable() {
        refreshTable(null);
    }

    private void refreshTable(String query) {
        List<Car> cars = carApi.listCars(query);
        if (cars == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка сети", "Не удалось загрузить список автомобилей. Проверьте, запущен ли сервер.");
            return;
        }
        table.getItems().setAll(cars);
    }

    private MenuBar createMenuBar() {
        Menu helpMenu = new Menu("Справка");

        MenuItem aboutItem = new MenuItem("О программе");
        aboutItem.setOnAction(e -> showAbout());

        MenuItem authorItem = new MenuItem("Об авторе");
        authorItem.setOnAction(e -> showAuthor());

        helpMenu.getItems().addAll(aboutItem, authorItem);

        return new MenuBar(helpMenu);
    }

    private void showAbout() {
        showAlert(Alert.AlertType.INFORMATION, "О программе",
                "Автопрокат\nКлиент-серверное приложение для курсовой работы\n" +
                        "Версия 1.0\n" +
                        "Сервер: Spring Boot + H2\n" +
                        "Клиент: JavaFX");
    }

    private void showAuthor() {
        showAlert(Alert.AlertType.INFORMATION, "Об авторе",
                "Разработчик: Студент бакалавриата\n" +
                        "Факультет ИТ и анализа больших данных\n" +
                        "Направление: Прикладная информатика\n" +
                        "Курсовая работа по дисциплине 'Современные технологии программирования'");
    }

    private boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void restoreWindowState(Stage stage) {
        Preferences prefs = Preferences.userRoot().node(PREFS_NODE);
        double w = prefs.getDouble(PREF_WIDTH, 900);
        double h = prefs.getDouble(PREF_HEIGHT, 600);
        double x = prefs.getDouble(PREF_X, Double.NaN);
        double y = prefs.getDouble(PREF_Y, Double.NaN);

        stage.setWidth(w);
        stage.setHeight(h);
        if (!Double.isNaN(x) && !Double.isNaN(y)) {
            stage.setX(x);
            stage.setY(y);
        }
    }

    private void saveWindowState(Stage stage) {
        Preferences prefs = Preferences.userRoot().node(PREFS_NODE);
        prefs.putDouble(PREF_WIDTH, stage.getWidth());
        prefs.putDouble(PREF_HEIGHT, stage.getHeight());
        prefs.putDouble(PREF_X, stage.getX());
        prefs.putDouble(PREF_Y, stage.getY());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
