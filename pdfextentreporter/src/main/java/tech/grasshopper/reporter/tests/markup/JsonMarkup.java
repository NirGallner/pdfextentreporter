package tech.grasshopper.reporter.tests.markup;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class JsonMarkup extends MarkupDisplay {

	private String html;

	@Override
	public AbstractCell displayDetails() {

		return TextCell.builder().text(jsonText()).textColor(textColor).fontSize(LOG_FONT_SIZE).font(LOG_FONT)
				.lineSpacing(MULTILINE_SPACING).build();
	}

	private String jsonText() {
		String jsonStringHolder = html.substring(html.indexOf("JSONTree"));
		int startIndex = jsonStringHolder.indexOf('{');
		int endIndex = startIndex;
		int bktCnt = 0;

		for (char c : jsonStringHolder.substring(startIndex).toCharArray()) {
			endIndex++;
			if (c == '{')
				bktCnt++;
			else if (c == '}')
				bktCnt--;
			if (bktCnt == 0)
				break;
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(JsonParser.parseString(jsonStringHolder.substring(startIndex, endIndex)));
	}
}
