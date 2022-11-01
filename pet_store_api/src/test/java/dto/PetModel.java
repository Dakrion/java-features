package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetModel {

	@Builder.Default
	List<String> photoUrls = List.of("https://example.com");

	@Builder.Default
	String name = "Вася";

	@Builder.Default
	Long id = 12345L;

	@Builder.Default
	Category category = Category.builder().build();

	@Builder.Default
	List<TagsItem> tags = List.of(TagsItem.builder().build());

	@Builder.Default
	String status = "available";

	@Value
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Category {

		@Builder.Default
		String name = "category";

		@Builder.Default
		int id = 1;
	}

	@Value
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TagsItem {

		@Builder.Default
		String name = "tags";

		@Builder.Default
		int id = 2;
	}
}