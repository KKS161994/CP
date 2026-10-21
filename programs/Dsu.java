import java.util.*;

/**
 * Disjoint Set Union (Union-Find) with path compression and union by size.
 *
 * Supports:
 *   - find(x): find the representative of x's component (with path halving)
 *   - union(x, y): merge the components of x and y
 *   - connected(x, y): whether x and y are in the same component
 *   - component_size(x): size of the component containing x
 */
public class Dsu {
    private final int[] parent;
    private final int[] size;
    private int components;

    public Dsu(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        components = n;
    }

    public int find(int x) {
        while (x != parent[x]) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    public boolean union(int x, int y) {
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) {
            return false;
        }
        if (size[rx] < size[ry]) {
            int tmp = rx;
            rx = ry;
            ry = tmp;
        }
        parent[ry] = rx;
        size[rx] += size[ry];
        components -= 1;
        return true;
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public int componentSize(int x) {
        return size[find(x)];
    }

    public int components() {
        return components;
    }
}
