cd ~/gudu
git pull
lein install
cd ..

cd ~/edgekite.web
git pull
lein immutant run -Dhttp.port=2000 -b=0.0.0.0 -bmanagement=0.0.0.0 &> ../immutant.out &
cd ..
