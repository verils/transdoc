package com.github.verils.transdoc.core.converter;

import com.github.verils.transdoc.core.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarkdownConverterTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testConvert() {
        Converter converter = new MarkdownConverter();

        WordDocument wordDocument = mock(WordDocument.class);

        Part titleParagraph = mockTitleParagraph();
        Part textParagraph = mockTextParagraph();
        Part codeBlock = mockSingleCellTable();
        Part table = mockTable();

        when(wordDocument.getParts()).thenReturn(
            Arrays.asList(
                titleParagraph,
                textParagraph,
                codeBlock,
                table
            )
        );

        String content = converter.convert(wordDocument);
        System.out.println(content);
        assertNotNull(content);
    }

    private ParagraphPart mockTitleParagraph() {
        ParagraphPart paragraph = mock(ParagraphPart.class);
        when(paragraph.isTitle()).thenReturn(true);
        when(paragraph.getTitleLevel()).thenReturn(1);
        when(paragraph.getText()).thenReturn("Title 1");
        return paragraph;
    }

    private ParagraphPart mockTextParagraph() {
        ParagraphPart paragraph = mock(ParagraphPart.class);
        when(paragraph.isTitle()).thenReturn(false);

        TextPiece boldText = mockBoldTextPiece("Bold Text");
        TextPiece italicText = mockItalicTextPiece("Italic Text");
        TextPiece text = mockTextPiece("Text");

        when(paragraph.getTextPieces()).thenReturn(Arrays.asList(boldText, italicText, text));
        return paragraph;
    }

    private Part mockSingleCellTable() {
        TablePart table = mock(TablePart.class);
        when(table.isSingleCellTable()).thenReturn(true);

        TableCell cell = mock(TableCell.class);
        List<Part> parts = mockSingleCellParts();
        when(cell.getParts()).thenReturn(parts);

        when(table.getCell(0, 0)).thenReturn(cell);
        return table;
    }

    private Part mockTable() {
        TablePart table = mock(TablePart.class);

        TableCell cell00 = mock(TableCell.class);
        List<Part> parts00 = mockCellParts();
        when(cell00.getParts()).thenReturn(parts00);

        TableCell cell01 = mock(TableCell.class);
        List<Part> parts01 = mockCellParts();
        when(cell01.getParts()).thenReturn(parts01);

        TableCell cell10 = mock(TableCell.class);
        List<Part> parts10 = mockCellParts();
        when(cell10.getParts()).thenReturn(parts10);

        TableCell cell11 = mock(TableCell.class);
        List<Part> parts11 = mockCellParts();
        when(cell11.getParts()).thenReturn(parts11);

        when(table.getRows()).thenReturn(2);
        when(table.getCols()).thenReturn(2);
        when(table.getCell(0, 1)).thenReturn(cell11);
        when(table.getCell(0, 0)).thenReturn(cell11);
        when(table.getCell(0, 1)).thenReturn(cell11);
        when(table.getCell(1, 0)).thenReturn(cell11);
        when(table.getCell(1, 1)).thenReturn(cell11);
        return table;
    }

    private TextPiece mockBoldTextPiece(String text) {
        TextPiece boldText = mock(TextPiece.class);
        when(boldText.isBold()).thenReturn(true);
        when(boldText.getText()).thenReturn(text);
        return boldText;
    }

    private TextPiece mockItalicTextPiece(String text) {
        TextPiece italicText = mock(TextPiece.class);
        when(italicText.isItalic()).thenReturn(true);
        when(italicText.getText()).thenReturn(text);
        return italicText;
    }

    private TextPiece mockTextPiece(String text) {
        TextPiece textPiece = mock(TextPiece.class);
        when(textPiece.getText()).thenReturn(text);
        return textPiece;
    }

    private List<Part> mockSingleCellParts() {
        ParagraphPart paragraph = mock(ParagraphPart.class);
        when(paragraph.getText()).thenReturn("This is code block.");
        return Collections.singletonList((Part) paragraph);
    }

    private List<Part> mockCellParts() {
        ParagraphPart paragraph = mock(ParagraphPart.class);
        TextPiece text = mockTextPiece("This is table cell.");
        List<TextPiece> textPieces = Collections.singletonList(text);
        when(paragraph.getTextPieces()).thenReturn(textPieces);
        return Collections.singletonList((Part) paragraph);
    }
}
