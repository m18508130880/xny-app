for i in `ps ax|awk '/java LNGAPPRMI.M/{print $1}'`; do
    kill -9 $i
done
