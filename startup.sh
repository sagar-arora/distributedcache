java -cp "target\distributedcache-1.0-SNAPSHOT.jar;target\lib\*" com.github.arorasagar.distributedcache.Server

mvn compile exec:java -Dexec.mainClass="com.github.arorasagar.distributedcache.Server" -Dexec.args="Config.json"