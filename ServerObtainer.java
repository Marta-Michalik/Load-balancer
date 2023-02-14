package pl.edu.agh.kt;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ServerObtainer {

	public static int getServer(boolean probesWithInterval) {
		List<Integer> servers = Arrays.asList(1, 2, 3);
		List<Integer> numberOfConnectionsForServers = Arrays.asList(1, 1, 1);
		List<Double> serversWeights = Arrays.asList(0.0, 0.0, 0.0);
		for (int j = 1; j < 4; j++) {
			double load = getServerLoad(j, probesWithInterval);
			serversWeights.set(j - 1, load);
		}
		int chosenServer = weightedLeastConnection(servers, serversWeights,
				numberOfConnectionsForServers);
		
		return chosenServer;
	}

	public static int weightedLeastConnection(List<Integer> servers,
			List<Double> serversWeights,
			List<Integer> numberOfConnectionsForServers) {
		int numberOfServers = servers.size();

		for (int server : servers) {
			if (serversWeights.get(server - 1) > 0) {
				for (int i = server + 1; i <= numberOfServers; i++) {
					if (numberOfConnectionsForServers.get(server - 1)
							* serversWeights.get(i - 1) < numberOfConnectionsForServers
							.get(i - 1) * serversWeights.get(server - 1)) {
						server = i;
					}
				}
				return server;
			}
		}
		throw new RuntimeException(
				"Could not find any server with non-zero weight");
	}

	public static double getServerLoad(int server, boolean withInterval) {
		double previousValue = MapForValues.getValue(server);
		Random rand = new Random();
		double upperbound;
		double newValueCandidate;
		if (withInterval) {
			upperbound = 0.1;
			newValueCandidate = previousValue + rand.nextDouble() * upperbound
					- (upperbound / 2);
		} else {
			upperbound = 1;
			newValueCandidate = rand.nextDouble() * upperbound;
		}

		if (newValueCandidate >= 0 && newValueCandidate <= 1) {
			MapForValues.setValue(server, newValueCandidate);
			return newValueCandidate;
		} else if (newValueCandidate < 0) {
			MapForValues.setValue(server, newValueCandidate * (double) (-1));
			return newValueCandidate * (double) (-1);
		} else {
			MapForValues.setValue(server, (double) 2 - newValueCandidate);
			return (double) 2 - newValueCandidate;
		}
	}

}
