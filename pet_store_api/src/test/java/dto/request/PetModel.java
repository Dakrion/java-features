package dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetModel {

	@Builder.Default
	private List<String> photoUrls = List.of("https://example.com");

	@Builder.Default
	private String name = "Вася";

	@Builder.Default
	private Long id = 12345L;

	@Builder.Default
	private Category category = Category.builder().build();

	@Builder.Default
	private List<TagsItem> tags = List.of(TagsItem.builder().build());

	@Builder.Default
	private String status = "available";

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Category {

		@Builder.Default
		private String name = "category";

		@Builder.Default
		private int id = 1;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TagsItem {

		@Builder.Default
		private String name = "tags";

		@Builder.Default
		private int id = 2;
	}
}