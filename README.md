# SlidingPuzzle (puzzle przesuwne)
Aby uruchomić program z poziomu kodu należy uruchomić plik z folderu src, SlidingPuzzle.java

Cały projekt był tworzony w środowisku IntelliJIdea w Javie (wersja 15).

Program posiada różne tryby działania, które zmienia się przełącznikiem wyszczególnionym w kodzie.

W jednym z trybów program miesza układankę puzzle przesuwne (domyślnie jest to 1000 ruchów mieszających).
Następnie za pomocą wybranej heurystyki (MisplacedTiles lub Manhattan), doprowadza układankę do stanu pierwotnego, robiąc to
w minimalną liczbę ruchów. Po ułożeniu, na wyjściu wyświetla się ścieżka rozwiązania oraz statystyki (czas, ilość odwiedzonych stanów).

Drugi tryb wykonuje porównanie działania heurystyk MisplacedTiles i Manhattan tworząc 100 losowych układanek. Uśrednione wyniki
są wypisywane na wyjściu.

Jest jeszcze tryb, w którym można samodzielnie ustawić ułożenie klocków w układance, jednak tryb ten nie jest jeszcze w pełni dopracowany.
