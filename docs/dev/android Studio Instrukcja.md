# Instalacja w Android Studio

1. Projekt należy pobrać z repozytorium używając polecenia w konsoli systemowej
    ```sh
        git clone https://github.com/autyzm-pg/friendly-plans.git
    ```
2. W Android Studio w widoku powitalnym należy wybrać polecenie **__Import Project (Eclipse ADT, Gradle, etc.)__**
3. Następnie należy wybrać folder **__src w głównym__** folderze projektu (równoległy do __docs__) i zatwierdzić
4. Może pojawić się informacja o tym, że projekt używa starej wersji Gradle. Należy ją zupdate'ować.
5. Po pierwszej próbie synchronizacji projektu może pojawić się błąd **__Error:Gradle version 2.10 is required. Current version is 2.4. If using the gradle wrapper, try editing the distributionUrl in ...__**. Należy wybrać opcję **__Fix Gradle Wrapper and re-import the project__**

6. Po tym projekt powinien być gotowy do użytku. Android Studio automatycznie dodaje opcje uruchamiania projektu app oraz appManager.
