#!/bin/sh

mkdir carpeta_temporal_speedtest

cd carpeta_temporal_speedtest

# 1. Detectar la arquitectura de la CPU
ARCH=$(uname -m)
DOWNLOAD_URL=""

case "$ARCH" in
    aarch64)
        DOWNLOAD_URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-aarch64.tgz"
        ;;
    armv7l|armv6l) # armv7l es común para ARM 32-bit
        DOWNLOAD_URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-armhf.tgz"
        ;;
    mips)
        DOWNLOAD_URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-mips.tgz"
        ;;
    mipsel)
        DOWNLOAD_URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-mipsel.tgz"
        ;;
    x86_64)
        DOWNLOAD_URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-x86_64.tgz"
        ;;
    i386)
        DOWNLOAD_URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-i386.tgz"
        ;;
    *)
        echo "Error: Arquitectura ($ARCH) no soportada o desconocida por este script."
        exit 1
        ;;
esac

if [ -z "$DOWNLOAD_URL" ]; then
    echo "Error: No se pudo determinar la URL de descarga para la arquitectura $ARCH."
    exit 1
fi

echo "Detectada arquitectura: $ARCH"
echo "Descargando Speedtest CLI desde: $DOWNLOAD_URL"

# 2. Descargar el archivo
wget -O speedtest.tgz "$DOWNLOAD_URL"

if [ $? -ne 0 ]; then
    echo "Error: Fallo al descargar speedtest.tgz."
    exit 1
fi

# 3. Descomprimir el archivo
# A veces el binario está directamente en el tar, otras veces en una carpeta.
# Intentamos primero mover el binario directamente, si no funciona, lo buscamos en una carpeta.

tar -xzf speedtest.tgz

# Verificar si 'speedtest' está en el directorio actual (común en OpenWrt)
if [ -f "speedtest" ]; then
    echo "Binario 'speedtest' encontrado directamente."
    SPEEDTEST_BIN="speedtest"
elif [ -f "./ookla-speedtest-*/speedtest" ]; then # Esto cubría el caso anterior donde falló
    echo "Binario 'speedtest' encontrado en subdirectorio."
    SPEEDTEST_BIN="./ookla-speedtest-*/speedtest"
else
    echo "Error: No se pudo encontrar el binario 'speedtest' después de descomprimir."
    rm speedtest.tgz # Limpiar
    exit 1
fi

# 4. Mover el ejecutable a /usr/bin
echo "Moviendo '$SPEEDTEST_BIN' a /usr/bin/"
mv "$SPEEDTEST_BIN" /usr/bin/

if [ $? -ne 0 ]; then
    echo "Error: Fallo al mover el binario a /usr/bin/. ¿Permisos?"
    rm -rf ./ookla-speedtest-*/ 2>/dev/null # Limpiar carpeta si existía
    rm speedtest.tgz # Limpiar
    exit 1
fi

# 5. Dar permisos de ejecución
chmod +x /usr/bin/speedtest

# 6. Limpiar archivos temporales
echo "Limpiando archivos temporales."
cd ..
rm -rf carpeta_temporal_speedtest # Intentar borrar la carpeta si existía

echo "Instalación de Speedtest CLI completada. Puedes ejecutar 'speedtest'."