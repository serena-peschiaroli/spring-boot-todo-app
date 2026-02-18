package com.serpes.springboottodoapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.serpes.springboottodoapp.models.TodoItem;
import com.serpes.springboottodoapp.repositories.TodoItemRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=LEGACY",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class SpringBootTodoAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TodoItemRepository todoItemRepository;

	@BeforeEach
	void setUp() {
		todoItemRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void todoCrudFlowWorks() throws Exception {
		mockMvc.perform(post("/todo")
				.param("title", "Write integration test"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		Iterable<TodoItem> afterCreate = todoItemRepository.findAll();
		TodoItem created = afterCreate.iterator().next();

		assertThat(created.getTitle()).isEqualTo("Write integration test");
		assertThat(created.isCompleted()).isFalse();
		assertThat(created.getCreateDate()).isNotNull();

		Instant originalCreateDate = created.getCreateDate();

		mockMvc.perform(post("/todo/{id}", created.getId())
				.param("id", String.valueOf(created.getId()))
				.param("title", "Updated title")
				.param("completed", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		TodoItem updated = todoItemRepository.findById(created.getId()).orElseThrow();
		assertThat(updated.getTitle()).isEqualTo("Updated title");
		assertThat(updated.isCompleted()).isTrue();
		assertThat(updated.getCreateDate()).isEqualTo(originalCreateDate);
		assertThat(updated.getModifiedDate()).isAfterOrEqualTo(originalCreateDate);

		mockMvc.perform(get("/delete/{id}", updated.getId()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		assertThat(todoItemRepository.findById(updated.getId())).isEmpty();
	}

}
