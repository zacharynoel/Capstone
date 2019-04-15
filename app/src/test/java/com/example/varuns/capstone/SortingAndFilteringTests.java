package com.example.varuns.capstone;

import com.example.varuns.capstone.model.Artisan;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SortingAndFilteringTests {

    @Test
    public void testGetArtisansNoDB() {
        menu_activity instance = new menu_activity();
        List<Artisan> artisans = instance.getArtisansNoDB();
        assertTrue(artisans.size() > 0);
        assertEquals(artisans.get(0).getFirstName(), instance.nameDB[0]);
    }

    @Test
    public void queryEmptyAdapter() {
        ArtisanAdapter adapter = new ArtisanAdapter(null, new LinkedList<Artisan>());
        adapter.simulateFilter("blah");
    }

    private ArtisanAdapter createAdapterWithArtisans() {
        ArtisanAdapter adapter = new ArtisanAdapter(null, new LinkedList<Artisan>());
        adapter.addArtisan(new Artisan(1, "Malcolm", "Craney", "bio", null, "911"));
        adapter.addArtisan(new Artisan(2, "Bruno", "Da Silva", "bio", null, "911"));
        adapter.addArtisan(new Artisan(2, "Cream", "Crusaders", "bio", null, "911"));

        return adapter;
    }
    @Test
    public void initializeData() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        assertEquals(adapter.getArtisans().size(), 3);
        assertEquals(1, adapter.simulateFilter("Cream").size());
    }

    @Test
    public void testEmptyQuery() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        assertEquals(3, adapter.simulateFilter("").size());
    }

    @Test
    public void testQuery1() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        List<Artisan> results = adapter.simulateFilter("m");
        assertEquals(1, results.size());
        assertEquals("Malcolm", results.get(0).getFirstName());
    }
    @Test
    public void testQuery2() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        List<Artisan> results = adapter.simulateFilter("b");

        assertEquals(1, results.size());
        assertEquals("Bruno", results.get(0).getFirstName());
    }
    @Test
    public void testQuery3() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        List<Artisan> results = adapter.simulateFilter("cre");
        assertEquals(1, results.size());
        assertEquals("Cream", results.get(0).getFirstName());
    }
    @Test
    public void testQuery4() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        List<Artisan> results = adapter.simulateFilter("Cream Crusaders");

        assertEquals(1, results.size());
        assertEquals("Cream", results.get(0).getFirstName());
    }

    @Test
    public void testQuery5() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        List<Artisan> results = adapter.simulateFilter("Craney");

        assertEquals(1, results.size());
        assertEquals("Malcolm", results.get(0).getFirstName());
    }

    @Test
    public void testAlphaFilter() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        adapter.sortAlphabetically();
        assertEquals(3, adapter.getArtisans().size());
        assertEquals(adapter.getArtisans().get(0).getFirstName(), "Bruno");
    }

    @Test
    public void testDateFilter() {
        ArtisanAdapter adapter = createAdapterWithArtisans();

        adapter.sortByDate();
        assertEquals(3, adapter.getArtisans().size());
        assertEquals(adapter.getArtisans().get(0).getFirstName(), "Malcolm");
    }
}