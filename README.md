monganez is MongoDB simple data mapper for Java. This library helps to convert Java Object(Map/Iterable/POJO) to the DBObject(from MongoDB Java Driver). For example, below.
<pre><code>
public class SaveTest {

	private DBCollection collection;
	
	@Before
	public void setUp() throws UnknownHostException, MongoException{
		Mongo mongo = new Mongo("localhost");
		DB db = mongo.getDB("test");
		collection = db.getCollection("saved");
	}
	
	@Test
	public void testNestedPojoListAsAttributeToDBObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String id = Long.toString(System.currentTimeMillis());
		Foo foo = new Foo();
		foo.setId(id);
		foo.setCount(40);
		Bar bar1 = new Bar();
		bar1.setId("bar1_" + id);
		bar1.setStringList(new ArrayList(Arrays.asList("hoge", "hogehoge")));
		Bar bar2 = new Bar();
		bar2.setId("bar2_" + id);
		bar2.setStringList(new ArrayList(Arrays.asList("foo", "bar", "hoge")));
		foo.setBarList(new ArrayList(Arrays.asList(bar1, bar2)));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("foo", foo);
		
		DBObjectEncoder encoder = new DBObjectEncoder();
		collection.save(encoder.encode(map));
	}
}
</code></pre>
In MongoDB, below. 
<pre><code>
> db.saved.find();
{ "_id" : ObjectId("4dda9b15f769a51d9f924c8d"), "foo" : { "id" : "1306172181493", "count" : 40, "class" : "net.wrap_trap.monganez.Foo", "bar" : null, "barList": { "collectionValue" : [
        {
                "id" : "bar1_1306172181493",
                "stringList" : {
                        "collectionValue" : [
                                "hoge",
                                "hogehoge"
                        ],
                        "collectionClass" : "java.util.ArrayList"
                },
                "class" : "net.wrap_trap.monganez.Bar"
        },
        {
                "id" : "bar2_1306172181493",
                "stringList" : {
                        "collectionValue" : [
                                "foo",
                                "bar",
                                "hoge"
                        ],
                        "collectionClass" : "java.util.ArrayList"
                },
                "class" : "net.wrap_trap.monganez.Bar"
        }
], "collectionClass" : "java.util.ArrayList" } } }
</code></pre>
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
