package eu.europeana.uim.store.mongo;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Ignore;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;

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
		IDirectory artifactStorePath = new FixedPath("/tmp/.embedded");
		
		try {
			
			conf = new MongodConfigBuilder().version(Version.V2_0_9)
					.net(new Net(port, false)).build();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ITempNaming executableNaming = new UUIDTempNaming();

		Command command = Command.MongoD;

		IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
		    .defaults(command)
		    .artifactStore(new ArtifactStoreBuilder()
		        .defaults(command)
		        .download(new DownloadConfigBuilder()
		            .defaultsForCommand(command)
		            .artifactStorePath(artifactStorePath))
		        .executableNaming(executableNaming))
		    .build();

		MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);

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
