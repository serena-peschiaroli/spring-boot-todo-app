package com.serpes.springboottodoapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.serpes.springboottodoapp.models.AppUser;
import com.serpes.springboottodoapp.models.TodoItem;
import com.serpes.springboottodoapp.repositories.AppUserRepository;
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

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		todoItemRepository.deleteAll();
		appUserRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void loginPageIsPublic() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk());
	}

	@Test
	void registerPageIsPublic() throws Exception {
		mockMvc.perform(get("/register"))
				.andExpect(status().isOk());
	}

	@Test
	void userCanRegister() throws Exception {
		mockMvc.perform(post("/register")
				.with(csrf())
				.param("username", "newuser")
				.param("password", "newpass123")
				.param("confirmPassword", "newpass123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?registered"));

		AppUser savedUser = appUserRepository.findByUsername("newuser").orElseThrow();
		assertThat(savedUser.getRole()).isEqualTo("USER");
		assertThat(passwordEncoder.matches("newpass123", savedUser.getPassword())).isTrue();
	}

	@Test
	void duplicateUsernameRegistrationIsRejected() throws Exception {
		appUserRepository.save(new AppUser("existinguser", passwordEncoder.encode("password123"), "USER"));

		mockMvc.perform(post("/register")
				.with(csrf())
				.param("username", "existinguser")
				.param("password", "anotherpass123")
				.param("confirmPassword", "anotherpass123"))
				.andExpect(status().isOk());

		assertThat(appUserRepository.findByUsername("existinguser")).isPresent();
		assertThat(appUserRepository.findAll()).hasSize(1);
	}

	@Test
	void todoCrudFlowWorks() throws Exception {
		mockMvc.perform(post("/todo")
				.with(user("admin").roles("ADMIN"))
				.with(csrf())
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
				.with(user("admin").roles("ADMIN"))
				.with(csrf())
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

		mockMvc.perform(get("/delete/{id}", updated.getId())
				.with(user("admin").roles("ADMIN")))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		assertThat(todoItemRepository.findById(updated.getId())).isEmpty();
	}

}
