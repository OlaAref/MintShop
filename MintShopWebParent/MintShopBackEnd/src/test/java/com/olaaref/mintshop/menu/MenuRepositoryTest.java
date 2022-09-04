package com.olaaref.mintshop.menu;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Menu;
import com.olaaref.mintshop.common.entity.MenuType;
import com.olaaref.mintshop.dao.MenuRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class MenuRepositoryTest {

	@Autowired
	private MenuRepository menuRepository;
	
	@Test
	public void getMenusAfterPositionTest() {
		List<Menu> menus = menuRepository.findMenusAfterPositionForMenuType(3, MenuType.FOOTER);
		System.out.println(menus.isEmpty());
		menus.forEach(System.out::println);
	}
	
	@Test
	public void changeEnabledStatusTest() {
		String alias = "about_us";
		menuRepository.updateEnabledStatus(alias, false);
	}
	
	@Test
	public void getLastPositionTest() {
		Menu menu = menuRepository.findFirstByMenuTypeOrderByPositionDesc(MenuType.HEADER);
		System.out.println(menu.getPosition());
	}
}
