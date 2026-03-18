package ru.fa.carrental.client;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.fa.carrental.client.api.CarApi;
import ru.fa.carrental.client.model.Car;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Окно статистики, отображает количество автомобилей по категориям.
 */
public class StatisticsWindow {

    private final CarApi carApi;

    public StatisticsWindow(CarApi carApi) {
        this.carApi = carApi;
    }

    public void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Статистика");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Категория");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Количество автомобилей");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);

        Button refresh = new Button("Обновить");
        refresh.setOnAction(e -> loadData(chart));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setCenter(chart);
        root.setBottom(refresh);

        loadData(chart);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void loadData(BarChart<String, Number> chart) {
        List<Car> cars = carApi.listCars(null);
        if (cars == null) {
            chart.getData().clear();
            return;
        }

        Map<String, Integer> counts = new HashMap<>();
        for (Car car : cars) {
            String category = car.getCategory() != null ? car.getCategory().getName() : "Без категории";
            counts.put(category, counts.getOrDefault(category, 0) + 1);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        counts.forEach((category, count) -> series.getData().add(new XYChart.Data<>(category, count)));

        chart.setData(FXCollections.observableArrayList(series));
    }
}
