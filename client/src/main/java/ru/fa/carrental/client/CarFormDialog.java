package ru.fa.carrental.client;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.fa.carrental.client.api.CarApi;
import ru.fa.carrental.client.model.Car;
import ru.fa.carrental.client.model.CarCategory;

import java.util.List;

/**
 * Диалоговая форма для добавления или редактирования автомобиля.
 *
 * <p>При сохранении выполняется запрос к REST API и вызывается колбэк {@code onSaved}.</p>
 */
public class CarFormDialog extends Dialog<Void> {
    /**
     * @param car        редактируемый автомобиль (null для создания нового)
     * @param api        клиент для взаимодействия с сервером
     * @param onSaved    колбэк, вызываемый после успешного сохранения
     * @param categories список доступных категорий (родительская сущность)
     */
    public CarFormDialog(Car car, CarApi api, Runnable onSaved, List<CarCategory> categories) {
        setTitle(car == null ? "Добавить автомобиль" : "Редактировать автомобиль");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField model = new TextField();
        TextField type = new TextField();
        ComboBox<CarCategory> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(categories);
        categoryCombo.setPromptText("Категория");

        TextField year = new TextField();
        TextField price = new TextField();
        CheckBox available = new CheckBox("Доступен");

        if (car != null) {
            model.setText(car.getModel());
            type.setText(car.getType());
            if (car.getCategory() != null) {
                categories.stream()
                        .filter(c -> c.getId() == car.getCategory().getId())
                        .findFirst()
                        .ifPresent(categoryCombo.getSelectionModel()::select);
            }
            year.setText(String.valueOf(car.getYear()));
            price.setText(String.valueOf(car.getPricePerDay()));
            available.setSelected(car.isAvailable());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Модель:"), 0, 0);
        grid.add(model, 1, 0);
        grid.add(new Label("Тип:"), 0, 1);
        grid.add(type, 1, 1);
        grid.add(new Label("Категория:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Год:"), 0, 3);
        grid.add(year, 1, 3);
        grid.add(new Label("Цена/день:"), 0, 4);
        grid.add(price, 1, 4);
        grid.add(available, 1, 5);

        getDialogPane().setContent(grid);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    int yearValue = Integer.parseInt(year.getText().trim());
                    double priceValue = Double.parseDouble(price.getText().trim());
                    Car toSave = car == null ? new Car() : car;
                    toSave.setModel(model.getText().trim());
                    toSave.setType(type.getText().trim());
                    toSave.setCategory(categoryCombo.getValue());
                    toSave.setYear(yearValue);
                    toSave.setPricePerDay(priceValue);
                    toSave.setAvailable(available.isSelected());

                    if (car == null) {
                        api.createCar(toSave);
                    } else {
                        api.updateCar(toSave);
                    }

                    onSaved.run();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Неверный формат данных");
                    alert.setContentText("Проверьте введенные значения для года и цены.");
                    alert.showAndWait();
                }
            }
            return null;
        });
    }
}
