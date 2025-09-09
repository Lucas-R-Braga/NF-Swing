@echo off
echo ==========================================
echo Compilando o projeto NF-Swing...
echo ==========================================

:: Limpa a pasta de saída
rmdir /s /q out 2>nul
mkdir out

:: Compila todos os .java com a lib do iText
echo Iniciando compilacao...
javac -verbose -d out -cp "itextpdf-5.5.13.3.jar" src\*.java src\ui\*.java src\repository\*.java src\entities\*.java src\util\*.java


if %errorlevel% neq 0 (
    echo ==========================================
    echo ERRO NA COMPILACAO!
    echo ==========================================
    pause
    exit /b %errorlevel%
)

echo ==========================================
echo Montando o FAT JAR (incluindo dependencias)...
echo ==========================================

:: Copia todas as classes compiladas para temp
mkdir temp
xcopy /E /I /Y out\* temp\

:: Extrai as classes do itextpdf dentro da temp
jar xf itextpdf-5.5.13.3.jar
xcopy /E /I /Y META-INF temp\META-INF
xcopy /E /I /Y com temp\com

:: Cria o JAR final com MANIFEST
jar cfm NF-Swing.jar MANIFEST.MF -C temp .

:: Limpeza dos temporários
rmdir /s /q temp
rmdir /s /q META-INF 2>nul
rmdir /s /q com 2>nul

echo ==========================================
echo Executando o aplicativo...
echo ==========================================
java -jar NF-Swing.jar

echo ==========================================
echo Finalizado.
echo ==========================================
pause
