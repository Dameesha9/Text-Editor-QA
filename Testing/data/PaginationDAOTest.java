package testing.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dal.PaginationDAO;
import dto.Pages;

import java.util.List;
import java.lang.reflect.Method;

public class PaginationDAOTest {

    private static final int PAGE_SIZE = 100;

    @Test
    void testPaginateWithExactPageSize() throws Exception {
        String content = "a".repeat(100);
        List<Pages> pages = invokePaginate(content);

        assertEquals(1, pages.size(), "Should create exactly 1 page for 100 chars");
        assertEquals(1, pages.get(0).getPageNumber(), "Page number should be 1");
        assertEquals(100, pages.get(0).getPageContent().length(),
                    "Page content should be exactly 100 characters");
    }

    @Test
    void testPaginateWithMultiplePages() throws Exception {
        String content = "a".repeat(250);
        List<Pages> pages = invokePaginate(content);

        assertEquals(3, pages.size(), "Should create 3 pages for 250 chars");
        assertEquals(100, pages.get(0).getPageContent().length(),
                    "First page should be 100 chars");
        assertEquals(100, pages.get(1).getPageContent().length(),
                    "Second page should be 100 chars");
        assertEquals(50, pages.get(2).getPageContent().length(),
                    "Third page should be 50 chars");
    }

    @Test
    void testPaginatePageNumbering() throws Exception {
        String content = "a".repeat(300);
        List<Pages> pages = invokePaginate(content);

        assertEquals(1, pages.get(0).getPageNumber(), "First page number should be 1");
        assertEquals(2, pages.get(1).getPageNumber(), "Second page number should be 2");
        assertEquals(3, pages.get(2).getPageNumber(), "Third page number should be 3");
    }

    @Test
    void testPaginateWithEmptyString() throws Exception {
        String content = "";
        List<Pages> pages = invokePaginate(content);

        assertEquals(1, pages.size(), "Empty string should create 1 page");
        assertEquals("", pages.get(0).getPageContent(), "Content should be empty");
        assertEquals(1, pages.get(0).getPageNumber(), "Page number should still be 1");
    }

    @Test
    void testPaginateWithNull() throws Exception {
        List<Pages> pages = invokePaginate(null);

        assertEquals(1, pages.size(), "Null content should create 1 page");
        assertEquals("", pages.get(0).getPageContent(), "Content should be empty for null");
        assertEquals(1, pages.get(0).getPageNumber(), "Page number should be 1");
    }

    @Test
    void testPaginateWithLessThanPageSize() throws Exception {
        String content = "Short content only 50 chars long aaaaaaaaaaaaaaaa";
        List<Pages> pages = invokePaginate(content);

        assertEquals(1, pages.size(), "Content < 100 chars should create 1 page");
        assertEquals(content, pages.get(0).getPageContent(),
                    "Content should match original");
        assertTrue(pages.get(0).getPageContent().length() < PAGE_SIZE,
                  "Page content should be less than page size");
    }

    @Test
    void testPaginateBoundaryExactly200Chars() throws Exception {
        String content = "a".repeat(200);
        List<Pages> pages = invokePaginate(content);

        assertEquals(2, pages.size(), "200 chars should create exactly 2 pages");
        assertEquals(100, pages.get(0).getPageContent().length());
        assertEquals(100, pages.get(1).getPageContent().length());
    }

    @Test
    void testPaginateBoundary201Chars() throws Exception {
        String content = "a".repeat(201);
        List<Pages> pages = invokePaginate(content);

        assertEquals(3, pages.size(), "201 chars should create 3 pages");
        assertEquals(100, pages.get(0).getPageContent().length());
        assertEquals(100, pages.get(1).getPageContent().length());
        assertEquals(1, pages.get(2).getPageContent().length());
    }

    @Test
    void testPaginateBoundary99Chars() throws Exception {
        String content = "a".repeat(99);
        List<Pages> pages = invokePaginate(content);

        assertEquals(1, pages.size(), "99 chars should create 1 page");
        assertEquals(99, pages.get(0).getPageContent().length());
    }

    @Test
    void testPaginateContentIntegrity() throws Exception {
        String content = "This is page one content. " + "a".repeat(74) +
                        "This is page two content. " + "b".repeat(74);
        List<Pages> pages = invokePaginate(content);

        StringBuilder reconstructed = new StringBuilder();
        for (Pages page : pages) {
            reconstructed.append(page.getPageContent());
        }

        assertEquals(content, reconstructed.toString(),
                    "Concatenated pages should match original content");
    }

    @Test
    void testPaginateWithSpecialCharacters() throws Exception {
        String content = "Special!@#$%^&*()_+-={}[]|:;<>?,./~`" + "a".repeat(64) +
                        "More special chars: \n\t\r" + "b".repeat(55);
        List<Pages> pages = invokePaginate(content);

        assertNotNull(pages, "Should handle special characters");
        assertTrue(pages.size() > 0, "Should create at least one page");
    }

    @Test
    void testPaginateWithUnicodeCharacters() throws Exception {
        String arabicText = "العربية " + "ا".repeat(92);
        List<Pages> pages = invokePaginate(arabicText);

        assertNotNull(pages, "Should handle Unicode characters");
        assertTrue(pages.size() > 0, "Should create pages for Unicode text");
    }

    @Test
    void testPaginateVeryLongContent() throws Exception {
        String content = "a".repeat(10000);
        List<Pages> pages = invokePaginate(content);

        assertEquals(100, pages.size(), "10000 chars should create 100 pages");

        for (int i = 0; i < pages.size(); i++) {
            assertEquals(i + 1, pages.get(i).getPageNumber(),
                        "Page numbers should be sequential");
            assertEquals(100, pages.get(i).getPageContent().length(),
                        "All pages should be 100 chars");
        }
    }

    private List<Pages> invokePaginate(String content) throws Exception {
        Method paginateMethod = PaginationDAO.class.getDeclaredMethod("paginate", String.class);
        paginateMethod.setAccessible(true);
        return (List<Pages>) paginateMethod.invoke(null, content);
    }
}
