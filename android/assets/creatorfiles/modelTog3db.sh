for file in *.fbx ; do
  ./fbx-conv-lin64 -f -o G3DB "$file" "../model/`basename $file .fbx`".g3db
done
