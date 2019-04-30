#!/bin/sh

# Test espresso - Written by S.M.Pericas

TMPFILE=/tmp/test.$$

rm *.class

for i in *.java
do
	FILENAME=`basename $i .java`
	echo "Compiling $i ..."
	espresso $i
	if [ $? = "0" ]; then
		echo -n "Testing $FILENAME ... "
		java $FILENAME > $TMPFILE
		diff "$FILENAME.output" $TMPFILE 2>&1 > /dev/null
		if [ $? != "0" ]; then
			echo "FAILED."
		else
			echo "PASSED."
		fi
	else
		echo "Error compiling file $i."
		exit 1
	fi
done

