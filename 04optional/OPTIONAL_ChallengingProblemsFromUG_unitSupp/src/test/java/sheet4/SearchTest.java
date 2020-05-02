package sheet4;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.io.Resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage") public class SearchTest {

	private ImmutableValueGraph<Integer, Integer> graph;

	@Before public void createGraph() throws IOException {
		graph = Search.readGraph(
				Resources.toString(Resources.getResource("map.txt"),
						StandardCharsets.UTF_8));
	}

	@Test public void testListAllNodes() {
		Assert.assertEquals(
				Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
						22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34),
				Search.listAllNodes(graph));
	}

	@Test public void testListAllEdges() {
		Assert.assertEquals(
				List.of(10, 8, 5, 2, 6, 4, 13, 4, 9, 5, 4, 4, 5, 6, 7, 4, 4, 3, 1, 8, 2, 4, 9, 10,
						8, 9, 3, 15, 4, 5, 7, 3, 11, 2, 1, 5, 4, 8, 2, 2, 3, 5, 4, 5, 3),
				Search.listAllEdges(graph));
	}


	@Test public void testFindAllNodeWith4OrMoreEdges() {
		Assert.assertEquals(
				List.of(6, 16, 17),
				Search.findAllNodeWith4OrMoreEdges(graph));
	}


	@Test public void testFindAllNodesWithEdgeSumGreaterThan20() {
		Assert.assertEquals(
				List.of(4, 6, 15, 16, 17),
				Search.findAllNodesWithEdgeSumGreaterThan20(graph));
	}


	@Test public void testShortestPath() {
		Assert.assertEquals(
				List.of(1, 2, 6, 7, 8, 12, 14, 18, 21, 34),
				Search.shortestPathFromSourceToDestination(graph, 1, 34));
	}
}