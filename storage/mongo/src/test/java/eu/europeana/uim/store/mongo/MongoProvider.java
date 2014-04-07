package eu.europeana.uim.store.mongo;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Ignore;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

/**
 * Class exposing Embedded Mongo
 * 
 * @author Yorgos.Mamakis@ europeana.eu
 * @since Apr 7 2014
 */
@Ignore
public class MongoProvider {
	private static boolean state;

	public MongoProvider(int port) {
		setupMongo(port);
	}

	private MongodExecutable mongodExecutable;

	private void setupMongo(int port) {

		IMongodConfig conf = null;
		try {
			conf = new MongodConfigBuilder().version(Version.V2_0_9)
					.net(new Net(port, false)).build();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		MongodStarter runtime = MongodStarter.getDefaultInstance();

		mongodExecutable = runtime.prepare(conf);
		try {
			if (!state) {
				mongodExecutable.start();
				state = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopMongo() {
		mongodExecutable.stop();
		state = false;
	}

}
