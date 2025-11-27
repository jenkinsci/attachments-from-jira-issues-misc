
@Test
public void testBold() throws IOException {
  assertThat(colorize("1mhello world"), is("<span class=\"ansicolor-bold\">hello world</span>"));
}

@Test
public void testItalics() throws IOException {
  assertThat(colorize("3mhello world"), is("<span class=\"ansicolor-italics\">hello world</span>"));
}

@Test
public void testUnderline() throws IOException {
  assertThat(colorize("4mhello world"), is("<span class=\"ansicolor-underline\">hello world</span>"));
}

@Test
public void testStrikethrough() throws IOException {
  assertThat(colorize("9mhello world"), is("<span class=\"ansicolor-strikethrough\">hello world</span>"));
}

@Test
public void testBlack() throws IOException {
  assertThat(colorize("30mhello world"), is("<span class=\"ansicolor-black\">hello world</span>"));
}

@Test
public void testRed() throws IOException {
  assertThat(colorize("31mhello world"), is("<span class=\"ansicolor-red\">hello world</span>"));
}

@Test
public void testGreen() throws IOException {
  assertThat(colorize("32mhello world"), is("<span class=\"ansicolor-green\">hello world</span>"));
}

@Test
public void testYellow() throws IOException {
  assertThat(colorize("33mhello world"), is("<span class=\"ansicolor-yellow\">hello world</span>"));
}

@Test
public void testBlue() throws IOException {
  assertThat(colorize("34mhello world"), is("<span class=\"ansicolor-blue\">hello world</span>"));
}

@Test
public void testMagenta() throws IOException {
  assertThat(colorize("35mhello world"), is("<span class=\"ansicolor-magenta\">hello world</span>"));
}

@Test
public void testCyan() throws IOException {
  assertThat(colorize("36mhello world"), is("<span class=\"ansicolor-cyan\">hello world</span>"));
}

@Test
public void testWhite() throws IOException {
  assertThat(colorize("37mhello world"), is("<span class=\"ansicolor-white\">hello world</span>"));
}

@Test
public void testBackgroundBlack() throws IOException {
  assertThat(colorize("40mhello world"), is("<span class=\"ansicolor-background-black\">hello world</span>"));
}

@Test
public void testBackgroundRed() throws IOException {
  assertThat(colorize("41mhello world"), is("<span class=\"ansicolor-background-red\">hello world</span>"));
}

@Test
public void testBackgroundGreen() throws IOException {
  assertThat(colorize("42mhello world"), is("<span class=\"ansicolor-background-green\">hello world</span>"));
}

@Test
public void testBackgroundYellow() throws IOException {
  assertThat(colorize("43mhello world"), is("<span class=\"ansicolor-background-yellow\">hello world</span>"));
}

@Test
public void testBackgroundBlue() throws IOException {
  assertThat(colorize("44mhello world"), is("<span class=\"ansicolor-background-blue\">hello world</span>"));
}

@Test
public void testBackgroundMagenta() throws IOException {
  assertThat(colorize("45mhello world"), is("<span class=\"ansicolor-background-magenta\">hello world</span>"));
}

@Test
public void testBackgroundCyan() throws IOException {
  assertThat(colorize("46mhello world"), is("<span class=\"ansicolor-background-cyan\">hello world</span>"));
}

@Test
public void testBackgroundWhite() throws IOException {
  assertThat(colorize("47mhello world"), is("<span class=\"ansicolor-background-white\">hello world</span>"));
}
