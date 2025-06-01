# Procesor Obrazów - Politechnika Wrocławska

Aplikacja desktopowa oparta na JavaFX do przetwarzania obrazów, stworzona jako projekt studencki na Politechnice Wrocławskiej. Umożliwia wczytywanie, edytowanie i zapisywanie obrazów z wykorzystaniem podstawowych operacji przetwarzania obrazu.

## 🎯 Funkcjonalności

- **Wczytywanie obrazów**: Obsługa plików JPG przez wybierak plików.
- **Operacje przetwarzania obrazów**:
  - **Negatyw** – odwraca kolory obrazu.
  - **Progowanie** – konwertuje obraz na czarno-biały na podstawie zadanego progu (0–255).
  - **Wykrywanie krawędzi** – zastosowanie operatora Sobela.
- **Transformacje obrazu**:
  - Obrót o 90° w lewo lub w prawo.
  - Skalowanie obrazu do niestandardowych rozmiarów (1–3000 px).
- **Zapisywanie obrazu**: Eksport przetworzonego obrazu do formatu JPG w folderze `Obrazy` użytkownika.
- **Interfejs graficzny**:
  - Intuicyjny i przejrzysty layout.
  - Nagłówek zawierający logo Politechniki Wrocławskiej (opcjonalne).

## 🖱 Użycie

1. **Uruchom aplikację** – klasa `HelloApplication`.
2. **Wczytaj obraz** – kliknij „Wybierz plik obrazka”.
3. **Zastosuj operacje**:
   - Wybierz z menu rozwijanego.
   - Dla progowania ustaw próg (0–255).
   - Kliknij „Wykonaj”.
4. **Transformuj obraz**:
   - Obróć obraz o 90°.
   - Skaluj do nowych wymiarów (1–3000 px).
5. **Zapisz obraz**:
   - Kliknij „Zapisz obraz” i podaj nazwę (3–100 znaków).
6. **Logi** – sprawdź plik `application.log`.


## 👤 Autor

**Stefan Wojciechowski**  
Politechnika Wrocławska, 2025
