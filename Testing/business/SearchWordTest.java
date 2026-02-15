package testing.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import bll.SearchWord;
import dto.Documents;
import dto.Pages;

import java.util.ArrayList;
import java.util.List;

public class SearchWordTest {

    private List<Documents> testDocuments;

    @BeforeEach
    void setUp() {
        testDocuments = new ArrayList<>();
    }

    @Test
    void testSearchKeywordFound() {
        Documents doc = createDocument(1, "TestDoc", "This is a test document with keyword");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("test", testDocuments);

        assertFalse(results.isEmpty(), "Should find the keyword");
        assertTrue(results.get(0).contains("test"), "Result should contain keyword");
    }

    @Test
    void testSearchKeywordNotFound() {
        Documents doc = createDocument(1, "TestDoc", "This is a sample document");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("missing", testDocuments);

        assertTrue(results.isEmpty(), "Should not find non-existent keyword");
    }

    @Test
    void testSearchKeywordMinimumLength() {
        Documents doc = createDocument(1, "TestDoc", "Some content here");
        testDocuments.add(doc);

        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("ab", testDocuments);
        }, "Should throw exception for keyword less than 3 characters");
    }

    @Test
    void testSearchKeywordExactlyThreeCharacters() {
        Documents doc = createDocument(1, "TestDoc", "The cat sat on the mat");
        testDocuments.add(doc);

        assertDoesNotThrow(() -> {
            List<String> results = SearchWord.searchKeyword("cat", testDocuments);
            assertFalse(results.isEmpty(), "Should find 3-character keyword");
        }, "Should accept exactly 3 characters");
    }

    @Test
    void testSearchKeywordCaseInsensitive() {
        Documents doc = createDocument(1, "TestDoc", "This document contains KEYWORD in caps");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);

        assertFalse(results.isEmpty(), "Should find keyword regardless of case");
    }

    @Test
    void testSearchKeywordMultipleDocuments() {
        testDocuments.add(createDocument(1, "Doc1", "First document with target word"));
        testDocuments.add(createDocument(2, "Doc2", "Second document with target word"));
        testDocuments.add(createDocument(3, "Doc3", "Third document without it"));

        List<String> results = SearchWord.searchKeyword("target", testDocuments);

        assertEquals(2, results.size(), "Should find keyword in 2 documents");
    }

    @Test
    void testSearchKeywordWithContext() {
        Documents doc = createDocument(1, "TestDoc", "The quick brown fox jumps");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("brown", testDocuments);

        assertFalse(results.isEmpty(), "Should find keyword");
        assertTrue(results.get(0).contains("quick brown"),
                  "Should include preceding word as context");
    }

    @Test
    void testSearchKeywordAtBeginning() {
        Documents doc = createDocument(1, "TestDoc", "keyword is at the beginning");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);

        assertFalse(results.isEmpty(), "Should find keyword at beginning");
        assertTrue(results.get(0).contains("keyword"), "Should contain the keyword");
    }

    @Test
    void testSearchKeywordInMultiplePages() {
        Documents doc = new Documents();
        doc.setId(1);
        doc.setName("MultiPageDoc");

        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, 1, 1, "First page with target"));
        pages.add(new Pages(2, 1, 2, "Second page content"));
        doc.setPages(pages);

        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("target", testDocuments);

        assertFalse(results.isEmpty(), "Should find keyword in first page");
    }

    @Test
    void testSearchKeywordEmptyDocumentList() {
        List<String> results = SearchWord.searchKeyword("keyword", new ArrayList<>());

        assertTrue(results.isEmpty(), "Empty document list should return empty results");
    }

    @Test
    void testSearchKeywordWithSpecialCharacters() {
        Documents doc = createDocument(1, "TestDoc", "Document with special@chars");
        testDocuments.add(doc);

        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("@#", testDocuments);
        }, "Should handle short special character searches");
    }

    @Test
    void testSearchKeywordWithNumbers() {
        Documents doc = createDocument(1, "TestDoc", "Document with number 123 inside");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("123", testDocuments);

        assertFalse(results.isEmpty(), "Should find numeric keywords");
    }

    @Test
    void testSearchKeywordStopsAtFirstMatch() {
        Documents doc = new Documents();
        doc.setId(1);
        doc.setName("Doc");

        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, 1, 1, "First page with keyword"));
        pages.add(new Pages(2, 1, 2, "Second page with keyword"));
        doc.setPages(pages);

        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);

        assertEquals(1, results.size(),
                    "Should return only first match per document");
    }

    @Test
    void testSearchKeywordWithWhitespace() {
        Documents doc = createDocument(1, "TestDoc", "Multiple   spaces   between   words");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("spaces", testDocuments);

        assertFalse(results.isEmpty(), "Should handle multiple whitespaces");
    }

    @Test
    void testSearchKeywordReturnsDocumentName() {
        Documents doc = createDocument(1, "MyDocument", "Content with keyword here");
        testDocuments.add(doc);

        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);

        assertFalse(results.isEmpty(), "Should find keyword");
        assertTrue(results.get(0).startsWith("MyDocument"),
                  "Result should start with document name");
    }

    private Documents createDocument(int id, String name, String content) {
        Documents doc = new Documents();
        doc.setId(id);
        doc.setName(name);

        List<Pages> pages = new ArrayList<>();
        Pages page = new Pages(1, id, 1, content);
        pages.add(page);
        doc.setPages(pages);

        return doc;
    }
}
