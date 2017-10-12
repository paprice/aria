#!/bin/bash

prefixe=${1%.*}
commande="pandoc "$1" -f markdown -o "$prefixe".pdf -N --toc -V lof -V lang=fr -V documentclass=scrreprt -V papersize=letter  -H header.txt"
$commande
