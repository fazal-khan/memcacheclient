# Memcacheclient

###Implemented memcache binary protocol for learning purpose
##Please note, I have not tested this code.

### Test code

<pre lang="java"><code>

public class TestMem {

    public static void main(String[] args) {
		testMem();
	}

	private static void testMem() {
		try (MemcacheClient c = new MemcacheClient()) {

			// test1(c, 20);
			// test2(c, 20);
			// test3(c, 20);
			// test4(c, 20);

			test5(c, 200);
			// test6(c);

			log("-------------done--------------------\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    static void test1(MemcacheClient c, int repeat) throws IOException {
		if (repeat <= 0)
			return;

		for (int i = 0; i < repeat; i++) {
			log("----test1----------------------------\n");
			// creats with value zero, delete it later.
			c.increment("myctr", 0, 10);

			log("value is    : ->" + c.getString("myctr") + "<-");
			log("value is    : ->" + c.getString("myctr") + "<-");
			log("value is    : ->" + c.getLong("myctr") + "<-");
			log("incr value is    : " + c.increment("myctr", 15));
			log("decr value is    : " + c.decrement("myctr", 10));
			log("value is    : ->" + c.getLong("myctr") + "<-");
			log("version is  : " + c.version());
			log("deleted ?   : " + c.delete("myctr"));
			log("deleted ?   : " + c.delete("myctr"));
			log("deleted ?   : " + c.delete("myctr"));
			log("------------------------------------\n");
		}
	}
    
    static void test2(MemcacheClient c, int repeat) throws IOException {
		if (repeat <= 0)
			return;

		for (int i = 0; i < repeat; i++) {

			log("----test2---------------------------\n");
			c.add("myctr", 10); // created with value zero, delete later.
			log("value is    : ->" + c.getString("myctr") + "<-");
			log("value is    : ->" + c.getLong("myctr") + "<-");
			log("incr value is    : " + c.increment("myctr", 15));
			log("decr value is    : " + c.decrement("myctr", 10));
			log("value is    : ->" + c.getLong("myctr") + "<-");
			log("version is  : " + c.version());
			log("deleted ?   : " + c.delete("myctr"));
			log("deleted ?   : " + c.delete("myctr"));
			log("deleted ?   : " + c.delete("myctr"));
			log("------------------------------------\n");
		}
	}
    
    static void test3(MemcacheClient c, int repeat) throws IOException {
		if (repeat <= 0)
			return;

		for (int i = 0; i < repeat; i++) {

			log("-----test3---------------------------\n");
			log("deleted ? : " + c.delete("Hello"));
			c.add("Hello", "World");
			log("value is :->" + c.getString("Hello") + "<-:");
			c.set("Hello", "Memcache!!");
			log("value is :->" + c.getString("Hello") + "<-:");
			c.replace("Hello", "World!");
			log("value is :->" + c.getString("Hello") + "<-:");
			c.append("Hello", " Dude!");
			log("value is :->" + c.getString("Hello") + "<-:");
			c.prepend("Hello", "My! ");
			log("value is :->" + c.getString("Hello") + "<-:");
			log("deleted ? : " + c.delete("Hello"));
			log("------------------------------------\n");
		}
	}
    
    static void test4(MemcacheClient c, int repeat) throws IOException {
		if (repeat <= 0)
			return;

		for (int i = 0; i < repeat; i++) {

			log("-----test4---------------------------\n");
			c.set("Hello1", "World1");
			c.set("Hello2", "World2");
			c.set("Hello3", "World3");
			c.set("Hello4", "World4");

			String[] result = c.mulitget("Hello1", "Hello2", "Hello3", "Hello4");
			log("multiget " + Arrays.toString(result));

			log("------------------------------------\n");
		}
	}

	static void test5(MemcacheClient c, int size) throws Exception {
		if (size <= 0)
			return;

		log("-----test5---------------------------\n");

		String[] keys = new String[size];
		for (int i = 0; i < size; i++) {
			keys[i] = "Hello_" + i;
			c.set(keys[i], "World_" + i);
		}

		String[] values = c.mulitget(keys);

		for (int i = 0; i < size; i++) {
			if (!("World_" + i).equals(values[i])) {
				log("Invalid value for " + keys[i] + " -- value is " + values[i]);
			} else {
				System.out.print(i + ",");
			}
		}

		System.out.println("");

		// delete them.
		c.delete(keys);

		log("------------------------------------\n");
	}

	static void test6(MemcacheClient c) throws Exception {
		log("-----test6---------------------------\n");
		c.quit();
		log("------------------------------------\n");
	}
    
    static void log(String msg) {
		System.out.println(msg);
	}
}
</code></pre>

