package dk.kea.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyRecipe
	{
		String answer;
		List<Map<String, String>> messages;

		public MyRecipe(String answer) {
			this.answer = answer;
		}
		public MyRecipe(String answer, List<Map<String,String>> messages) {
			this.answer = answer;
			this.messages = messages;
		}

		@Override
		public String toString()
			{
				return "MyRecipe{" +
						"answer='" + answer + '\'' +
						'}';
			}
	}
