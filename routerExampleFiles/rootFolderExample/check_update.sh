#!/bin/sh

# Detectar target (ej: ath79/generic)
target=$(grep DISTRIB_TARGET= /etc/openwrt_release | cut -d"'" -f2)

# Detectar nombre de la placa (ej: tplink,archer-c60-v3)
board=$(cat /tmp/sysinfo/board_name)

# Última versión estable disponible en el repo
latest=$(curl -fsS https://downloads.openwrt.org/releases/ \
    | grep -Eo 'href="[0-9]+\.[0-9]+\.[0-9]+/"' \
    | cut -d'"' -f2 | tr -d '/' \
    | sort -V | tail -1)

# Buscar imagen sysupgrade en la carpeta del target
url="https://downloads.openwrt.org/releases/$latest/targets/$target/"
image=$(curl -fsS "$url" | grep "sysupgrade.bin" | grep "$board" | cut -d'"' -f2)

if [ -n "$image" ]; then
    echo "URL Found"
    echo "$url$image"
    echo
    echo "command"
    echo "cd /tmp"
    echo "wget $url$image"
    echo "sysupgrade -n /tmp/$(basename $image)"
    echo "end_command"
else
    echo "\nNOT_FOUND\n"
fi
