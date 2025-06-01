//Sporo importów
package com.example.imageprocessorapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

//Przyciski i takie pierdoły
public class HelloApplication extends Application {

    private ImageView originalImageView;
    private ImageView processedImageView;
    private ComboBox<String> operationComboBox;
    private Button executeButton;
    private Button saveImageButton;
    private Button loadImageButton;
    private Button rotateLeftButton;
    private Button rotateRightButton;
    private Button scaleButton;

    private File currentImageFile;
    private Image originalImage;
    private Image processedImage;
    private boolean hasOperationsApplied = false;

    public static void main(String[] args) {
        logMessage("INFO", "Aplikacja została uruchomiona");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Processor - Politechnika Wrocławska");

        // Główny layout
        BorderPane root = new BorderPane();

        // Górna część - nagłówek z logo
        VBox header = createHeader();
        root.setTop(header);

        // Środek - główna funkcjonalność
        VBox center = createCenterContent();
        root.setCenter(center);

        // Dół - stopka z danymi autora
        Label footer = new Label("Autor: Stefan Wojciechowski - Politechnika Wrocławska 2025");
        footer.setStyle("-fx-font-size: 10px; -fx-padding: 10px;");
        root.setBottom(footer);

        Scene scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Logowanie zamknięcia aplikacji
        primaryStage.setOnCloseRequest(e -> {
            logMessage("INFO", "Aplikacja została zamknięta");
        });
    }
    //Tworzymy nagłówek z pięknym logo
    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setSpacing(10);
        header.setStyle("-fx-background-color: #f8f9fa;");

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setSpacing(15);

        // Wczytaj logo PWr
        ImageView logoView = loadPwrLogo();
        if (logoView != null) {
            titleBox.getChildren().add(logoView);
        } else {
            // Fallback - stylizowane logo tekstowe
            VBox stylizedLogo = createStylizedLogo();
            titleBox.getChildren().add(stylizedLogo);
        }

        Label title = new Label("Image Processor - Politechnika Wrocławska");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #003366;");

        titleBox.getChildren().add(title);

        Label welcome = new Label("Witaj w aplikacji do przetwarzania obrazów!");
        welcome.setFont(Font.font(14));
        welcome.setStyle("-fx-text-fill: #666666;");

        header.getChildren().addAll(titleBox, welcome);
        return header;
    }
    // Wczytujmey logo
    private ImageView loadPwrLogo() {
        String logoPath = "/images/pwr_logo.png";

        try {
            // Wczytaj logo z zasobów (resources)
            InputStream logoStream = getClass().getResourceAsStream(logoPath);
            if (logoStream != null) {
                Image logoImage = new Image(logoStream);
                logoStream.close();

                if (!logoImage.isError()) {
                    ImageView logoView = new ImageView(logoImage);
                    logoView.setFitHeight(60);
                    logoView.setFitWidth(80);
                    logoView.setPreserveRatio(true);
                    logoView.setSmooth(true);
                    logoView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0.5, 1, 1);");

                    logMessage("INFO", "Pomyślnie wczytano logo PWr z: " + logoPath);
                    return logoView;
                } else {
                    logMessage("ERROR", "Błąd wczytywania logo PWr z: " + logoPath);
                }
            } else {
                logMessage("ERROR", "Nie znaleziono pliku logo PWr w: " + logoPath);
            }
        } catch (Exception e) {
            logMessage("ERROR", "Błąd wczytywania logo PWr z: " + logoPath + " - " + e.getMessage());
        }

        logMessage("INFO", "Nie znaleziono pliku logo PWr, używam stylizowanego logo tekstowego");
        return null;
    }
    // Tworzymy logo pwr, zabawa w grafika przez problemy z zaśnięciem
    private VBox createStylizedLogo() {
        VBox logoContainer = new VBox();
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setMinWidth(80);
        logoContainer.setMinHeight(60);
        logoContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #003366, #004488);" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0.5, 2, 2);"
        );

        Label pwrText = new Label("PWr");
        pwrText.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-family: 'Arial Black', Arial, sans-serif;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitle = new Label("POLITECHNIKA");
        subtitle.setStyle(
                "-fx-text-fill: #FFD700;" +
                        "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 7px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-letter-spacing: 0.5px;"
        );

        Label subtitle2 = new Label("WROCŁAWSKA");
        subtitle2.setStyle(
                "-fx-text-fill: #FFD700;" +
                        "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 7px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-letter-spacing: 0.5px;"
        );

        logoContainer.getChildren().addAll(pwrText, subtitle, subtitle2);
        return logoContainer;
    }

    private VBox createCenterContent() {
        VBox center = new VBox();
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));
        center.setSpacing(20);

        // Sekcja wczytywania pliku
        VBox loadSection = new VBox();
        loadSection.setAlignment(Pos.CENTER);
        loadSection.setSpacing(10);

        Label loadLabel = new Label("1. Wczytaj obraz:");
        loadLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        loadImageButton = new Button("Wybierz plik obrazka");
        loadImageButton.setOnAction(e -> handleLoadImage());

        loadSection.getChildren().addAll(loadLabel, loadImageButton);

        // Sekcja operacji
        VBox operationSection = new VBox();
        operationSection.setAlignment(Pos.CENTER);
        operationSection.setSpacing(10);

        Label operationLabel = new Label("2. Wybierz operację:");
        operationLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        HBox operationBox = new HBox();
        operationBox.setAlignment(Pos.CENTER);
        operationBox.setSpacing(10);

        operationComboBox = new ComboBox<>();
        operationComboBox.getItems().addAll("Negatyw", "Progowanie", "Konturowanie");
        operationComboBox.setPromptText("Wybierz operację...");
        operationComboBox.setPrefWidth(200);

        executeButton = new Button("Wykonaj");
        executeButton.setDisable(true);
        executeButton.setOnAction(e -> handleExecuteOperation());

        operationBox.getChildren().addAll(operationComboBox, executeButton);

        // Przyciski obrotu
        HBox rotationBox = new HBox();
        rotationBox.setAlignment(Pos.CENTER);
        rotationBox.setSpacing(10);

        rotateLeftButton = new Button("↺ Obrót w lewo");
        rotateLeftButton.setDisable(true);
        rotateLeftButton.setOnAction(e -> handleRotateLeft());

        rotateRightButton = new Button("↻ Obrót w prawo");
        rotateRightButton.setDisable(true);
        rotateRightButton.setOnAction(e -> handleRotateRight());

        scaleButton = new Button("Skaluj obraz");
        scaleButton.setDisable(true);
        scaleButton.setOnAction(e -> handleScaleImage());

        rotationBox.getChildren().addAll(rotateLeftButton, rotateRightButton, scaleButton);

        operationSection.getChildren().addAll(operationLabel, operationBox, rotationBox);

        // Sekcja podglądu obrazów
        HBox imagePreview = new HBox();
        imagePreview.setAlignment(Pos.CENTER);
        imagePreview.setSpacing(20);

        VBox originalBox = new VBox();
        originalBox.setAlignment(Pos.CENTER);
        originalBox.setSpacing(10);

        Label originalLabel = new Label("Obraz oryginalny:");
        originalLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        originalImageView = new ImageView();
        originalImageView.setFitHeight(200);
        originalImageView.setFitWidth(200);
        originalImageView.setPreserveRatio(true);
        originalImageView.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

        originalBox.getChildren().addAll(originalLabel, originalImageView);

        VBox processedBox = new VBox();
        processedBox.setAlignment(Pos.CENTER);
        processedBox.setSpacing(10);

        Label processedLabel = new Label("Obraz po obróbce:");
        processedLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        processedImageView = new ImageView();
        processedImageView.setFitHeight(200);
        processedImageView.setFitWidth(200);
        processedImageView.setPreserveRatio(true);
        processedImageView.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

        processedBox.getChildren().addAll(processedLabel, processedImageView);

        imagePreview.getChildren().addAll(originalBox, processedBox);

        // Sekcja zapisu
        VBox saveSection = new VBox();
        saveSection.setAlignment(Pos.CENTER);
        saveSection.setSpacing(10);

        Label saveLabel = new Label("3. Zapisz obraz:");
        saveLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        saveImageButton = new Button("Zapisz obraz");
        saveImageButton.setDisable(true);
        saveImageButton.setOnAction(e -> handleSaveImage());

        saveSection.getChildren().addAll(saveLabel, saveImageButton);

        center.getChildren().addAll(loadSection, operationSection, imagePreview, saveSection);
        return center;
    }
    // Ładowanie obrazów
    private void handleLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz obraz");

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Pliki JPG (*.jpg)", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) loadImageButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String fileName = file.getName().toLowerCase();
                if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                    showToast("Niedozwolony format pliku");
                    logMessage("ERROR", "Próba wczytania pliku o niedozwolonym formacie: " + fileName);
                    return;
                }

                // Wyczyść poprzednie obrazy z pamięci
                if (originalImage != null) {
                    originalImage = null;
                    processedImage = null;
                    processedImageView.setImage(null);
                    hasOperationsApplied = false;
                }

                originalImage = new Image(file.toURI().toString());
                originalImageView.setImage(originalImage);
                currentImageFile = file;

                // Aktywuj przyciski
                executeButton.setDisable(false);
                saveImageButton.setDisable(false);
                rotateLeftButton.setDisable(false);
                rotateRightButton.setDisable(false);
                scaleButton.setDisable(false); // Aktywacja przycisku skalowania

                showToast("Pomyślnie załadowano plik");
                logMessage("INFO", "Wczytano plik: " + file.getName());

            } catch (Exception e) {
                showToast("Nie udało się załadować pliku");
                logMessage("ERROR", "Błąd wczytywania pliku: " + e.getMessage());
            }
        }
    }
    /* Progowanie, Negatyw, Konturowanie
    * Zrównoleglenianie operacji tworzy 4 wątki wykonujące to samo zadanie (85-89)
    *  */
    private void handleExecuteOperation() {
        String operation = operationComboBox.getValue();

        if (operation == null) {
            showToast("Nie wybrano operacji do wykonania");
            return;
        }

        if (originalImage == null) {
            showToast("Nie wczytano obrazu");
            return;
        }

        // Dla progowania pobierz próg w wątku JavaFX
        if (operation.equals("Progowanie")) {
            TextInputDialog dialog = new TextInputDialog("128");
            dialog.setTitle("Progowanie");
            dialog.setHeaderText("Podaj wartość progu (0-255)");
            dialog.setContentText("Próg:");

            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                showToast("Anulowano progowanie");
                return;
            }

            try {
                int threshold = Integer.parseInt(result.get());
                if (threshold < 0 || threshold > 255) {
                    showToast("Próg musi być z zakresu 0-255");
                    return;
                }

                // Wykonaj progowanie w osobnym wątku
                Task<Image> task = new Task<Image>() {
                    @Override
                    protected Image call() throws Exception {
                        Image sourceImage = processedImage != null ? processedImage : originalImage;
                        return processThresholding(sourceImage, threshold);
                    }
                };

                task.setOnSucceeded(e -> {
                    processedImage = task.getValue();
                    processedImageView.setImage(processedImage);
                    hasOperationsApplied = true;
                    showToast("Progowanie zostało przeprowadzone pomyślnie!");
                    logMessage("INFO", "Wykonano operację progowania z progiem: " + threshold);
                });

                task.setOnFailed(e -> {
                    showToast("Nie udało się wykonać operacji: Progowanie");
                    logMessage("ERROR", "Błąd operacji Progowanie: " + task.getException().getMessage());
                });

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();

            } catch (NumberFormatException e) {
                showToast("Podaj prawidłową liczbę");
                logMessage("ERROR", "Błąd wprowadzania progu: " + e.getMessage());
            }

            return;
        }

        // Wykonaj inne operacje w osobnym wątku
        Task<Image> task = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                Image sourceImage = processedImage != null ? processedImage : originalImage;

                switch (operation) {
                    case "Negatyw":
                        return processNegative(sourceImage);
                    case "Konturowanie":
                        return processEdgeDetection(sourceImage);
                    default:
                        throw new Exception("Nieznana operacja");
                }
            }
        };

        task.setOnSucceeded(e -> {
            processedImage = task.getValue();
            processedImageView.setImage(processedImage);
            hasOperationsApplied = true;

            switch (operation) {
                case "Negatyw":
                    showToast("Negatyw został wygenerowany pomyślnie!");
                    logMessage("INFO", "Wykonano operację negatywu");
                    break;
                case "Konturowanie":
                    showToast("Konturowanie zostało przeprowadzone pomyślnie!");
                    logMessage("INFO", "Wykonano operację konturowania");
                    break;
            }
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            showToast("Nie udało się wykonać operacji: " + operation);
            logMessage("ERROR", "Błąd operacji " + operation + ": " + exception.getMessage());
        });

        // Zrównoleglenie operacji (Zadanie 3)
        int maxThreads = 4;
        for (int i = 0; i < maxThreads; i++) {
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }
    //Obracamy w lewo
    private void handleRotateLeft() {
        if (originalImage != null) {
            Image sourceImage = processedImage != null ? processedImage : originalImage;
            processedImage = rotateImage(sourceImage, -90);
            processedImageView.setImage(processedImage);
            hasOperationsApplied = true;
            showToast("Obrót w lewo wykonano pomyślnie!");
            logMessage("INFO", "Wykonano obrót w lewo");
        }
    }
    // Obracamy w prawo
    private void handleRotateRight() {
        if (originalImage != null) {
            Image sourceImage = processedImage != null ? processedImage : originalImage;
            processedImage = rotateImage(sourceImage, 90);
            processedImageView.setImage(processedImage);
            hasOperationsApplied = true;
            showToast("Obrót w prawo wykonano pomyślnie!");
            logMessage("INFO", "Wykonano obrót w prawo");
        }
    }
    //Skalowanie zdjęć
    private void handleScaleImage() {
        if (originalImage == null) {
            showToast("Najpierw wczytaj obraz!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Skalowanie obrazu");
        dialog.setHeaderText("Podaj nowe wymiary obrazu");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField widthField = new TextField();
        widthField.setPromptText("Szerokość (px)");
        TextField heightField = new TextField();
        heightField.setPromptText("Wysokość (px)");

        // Ustaw aktualne wymiary jako domyślne
        Image sourceImage = processedImage != null ? processedImage : originalImage;
        widthField.setText(String.valueOf((int)sourceImage.getWidth()));
        heightField.setText(String.valueOf((int)sourceImage.getHeight()));

        Button restoreButton = new Button("Przywróć oryginalne wymiary");
        restoreButton.setOnAction(e -> {
            widthField.setText(String.valueOf((int)originalImage.getWidth()));
            heightField.setText(String.valueOf((int)originalImage.getHeight()));
        });

        grid.add(new Label("Szerokość:"), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(new Label("Wysokość:"), 0, 1);
        grid.add(heightField, 1, 1);
        grid.add(restoreButton, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String widthText = widthField.getText().trim();
            String heightText = heightField.getText().trim();

            if (widthText.isEmpty() || heightText.isEmpty()) {
                showToast("Wypełnij oba pola wymiarów!");
                return;
            }

            try {
                int width = Integer.parseInt(widthText);
                int height = Integer.parseInt(heightText);

                if (width <= 0 || height <= 0 || width > 3000 || height > 3000) {
                    showToast("Wymiary muszą być z zakresu 1-3000 pikseli");
                    return;
                }

                processedImage = scaleImage(sourceImage, width, height);
                processedImageView.setImage(processedImage);
                hasOperationsApplied = true;
                showToast("Skalowanie wykonano pomyślnie!");
                logMessage("INFO", "Wykonano skalowanie do " + width + "x" + height);

            } catch (NumberFormatException e) {
                showToast("Podaj prawidłowe liczby");
                logMessage("ERROR", "Błąd skalowania: " + e.getMessage());
            }
        }
    }
    //Zapisujemy
    private void handleSaveImage() {
        if (originalImage == null) {
            showToast("Brak obrazu do zapisania");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Zapisz obraz");
        dialog.setHeaderText("Podaj nazwę pliku");

        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(10));

        if (!hasOperationsApplied) {
            Label warning = new Label("Na pliku nie zostały wykonane żadne operacje!");
            warning.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
            content.getChildren().add(warning);
        }

        TextField nameField = new TextField();
        nameField.setPromptText("Nazwa pliku (3-100 znaków)");
        content.getChildren().addAll(new Label("Nazwa pliku:"), nameField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String fileName = nameField.getText().trim();
                if (fileName.length() < 3) {
                    Platform.runLater(() -> showToast("Wpisz co najmniej 3 znaki"));
                    return null;
                }
                if (fileName.length() > 100) {
                    Platform.runLater(() -> showToast("Nazwa nie może być dłuższa niż 100 znaków"));
                    return null;
                }
                return fileName;
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String fileName = result.get() + ".jpg";

            try {
                // Pobierz folder Obrazy
                String userHome = System.getProperty("user.home");
                File picturesDir = new File(userHome, "Pictures");
                if (!picturesDir.exists()) {
                    picturesDir = new File(userHome, "Obrazy"); // dla polskiego Windows
                }
                if (!picturesDir.exists()) {
                    picturesDir = new File(userHome); // fallback na folder domowy
                }

                File outputFile = new File(picturesDir, fileName);

                if (outputFile.exists()) {
                    showToast("Plik " + fileName + " już istnieje w systemie. Podaj inną nazwę pliku!");
                    return;
                }

                // Zapisz obraz
                Image imageToSave = processedImage != null ? processedImage : originalImage;
                saveImageToFile(imageToSave, outputFile);

                showToast("Zapisano obraz w pliku " + fileName);
                logMessage("INFO", "Zapisano obraz: " + outputFile.getAbsolutePath());

            } catch (Exception e) {
                showToast("Nie udało się zapisać pliku " + fileName);
                logMessage("ERROR", "Błąd zapisu pliku: " + e.getMessage());
            }
        }
    }

    // Operacje przetwarzania obrazu
    private Image processNegative(Image sourceImage) {
        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage negativeImage = new WritableImage(width, height);
        PixelReader pixelReader = sourceImage.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                Color negativeColor = new Color(
                        1.0 - color.getRed(),
                        1.0 - color.getGreen(),
                        1.0 - color.getBlue(),
                        color.getOpacity()
                );
                negativeImage.getPixelWriter().setColor(x, y, negativeColor);
            }
        }

        return negativeImage;
    }

    private Image processThresholding(Image sourceImage, int threshold) {
        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage thresholdImage = new WritableImage(width, height);
        PixelReader pixelReader = sourceImage.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                double gray = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();

                Color newColor = gray * 255 > threshold ? Color.WHITE : Color.BLACK;
                thresholdImage.getPixelWriter().setColor(x, y, newColor);
            }
        }

        return thresholdImage;
    }

    private Image processEdgeDetection(Image sourceImage) {
        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage edgeImage = new WritableImage(width, height);
        PixelReader pixelReader = sourceImage.getPixelReader();

        // Sobel operator
        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double gx = 0, gy = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = pixelReader.getColor(x + i, y + j);
                        double gray = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();

                        gx += gray * sobelX[i + 1][j + 1];
                        gy += gray * sobelY[i + 1][j + 1];
                    }
                }

                double magnitude = Math.sqrt(gx * gx + gy * gy);
                magnitude = Math.min(1.0, magnitude);

                Color edgeColor = new Color(magnitude, magnitude, magnitude, 1.0);
                edgeImage.getPixelWriter().setColor(x, y, edgeColor);
            }
        }

        return edgeImage;
    }

    private Image rotateImage(Image sourceImage, double angle) {
        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        // Dla obrotu o 90 stopni zamieniamy wymiary
        boolean swap = Math.abs(angle) == 90;
        int newWidth = swap ? height : width;
        int newHeight = swap ? width : height;

        WritableImage rotatedImage = new WritableImage(newWidth, newHeight);
        PixelReader pixelReader = sourceImage.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);

                int newX, newY;
                if (angle == 90) {
                    newX = height - 1 - y;
                    newY = x;
                } else if (angle == -90) {
                    newX = y;
                    newY = width - 1 - x;
                } else {
                    newX = x;
                    newY = y;
                }

                if (newX >= 0 && newX < newWidth && newY >= 0 && newY < newHeight) {
                    rotatedImage.getPixelWriter().setColor(newX, newY, color);
                }
            }
        }

        return rotatedImage;
    }

    private Image scaleImage(Image sourceImage, int newWidth, int newHeight) {
        WritableImage scaledImage = new WritableImage(newWidth, newHeight);
        PixelReader pixelReader = sourceImage.getPixelReader();

        double scaleX = sourceImage.getWidth() / (double) newWidth;
        double scaleY = sourceImage.getHeight() / (double) newHeight;

        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                int sourceX = (int) (x * scaleX);
                int sourceY = (int) (y * scaleY);

                sourceX = Math.min(sourceX, (int) sourceImage.getWidth() - 1);
                sourceY = Math.min(sourceY, (int) sourceImage.getHeight() - 1);

                Color color = pixelReader.getColor(sourceX, sourceY);
                scaledImage.getPixelWriter().setColor(x, y, color);
            }
        }

        return scaledImage;
    }

    private void saveImageToFile(Image image, File file) throws IOException {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        PixelReader pixelReader = image.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                int rgb = ((int) (color.getRed() * 255) << 16) |
                        ((int) (color.getGreen() * 255) << 8) |
                        ((int) (color.getBlue() * 255));
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(bufferedImage, "jpg", file);
    }

    private void showToast(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void logMessage(String level, String message) {
        try {
            File logFile = new File("application.log");
            FileWriter writer = new FileWriter(logFile, true);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(String.format("[%s] %s: %s%n", timestamp, level, message));
            writer.close();

        } catch (IOException e) {
            System.err.println("Nie można zapisać do pliku logów: " + e.getMessage());
        }
    }
}