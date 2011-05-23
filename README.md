monganez is MongoDB simple data mapper for Java.
## Usage
- encode Map/Iterable/POJO to DBObject.
- decode DBObject to Map/Iterable/POJO.
- show <a href="https://github.com/masayuki038/monganez/tree/master/src/test/java/net/wrap_trap/monganez">deails</a>.

## Make Jar
<pre><code>
mvn package -Dmaven.test.skip=true
</code></pre>

## Maven Repository
- Jars: http://wrap-trap.net/maven2/snapshot/net/wrap-trap/monganez/
- Repository URL: http://wrap-trap.net/maven2/snapshot/
<pre><code>
&lt;dependency&gt;
		&lt;groupId&gt;net.wrap-trap&lt;/groupId&gt;
		&lt;artifactId&gt;monganez&lt;/artifactId&gt;
		&lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;
		&lt;type&gt;jar&lt;/type&gt;
		&lt;scope&gt;compile&lt;/scope&gt;
&lt;/dependency&gt;
&lt;repository&gt;
		&lt;id&gt;wrap-trap.net/maven2/snapshot&lt;/id&gt;
		&lt;name&gt;wrap-trap.net Maven Snapshot Repository&lt;/name&gt;
		&lt;url&gt;http://wrap-trap.net/maven2/snapshot&lt;/url&gt;
&lt;/repository&gt;
</code></pre>

## License

ASL 2.0
