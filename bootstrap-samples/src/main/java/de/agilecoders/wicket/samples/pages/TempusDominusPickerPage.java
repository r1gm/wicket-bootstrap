package de.agilecoders.wicket.samples.pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTimeTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalTimeTextField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.LambdaChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.tempusdominus.AbstractTempusDominusWithIcon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.tempusdominus.TempusDominusBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.tempusdominus.TempusDominusConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.tempusdominus.TempusDominusDisplayConfig.ButtonType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.tempusdominus.TempusDominusDisplayConfig.ComponentType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.tempusdominus.TempusDominusDisplayConfig.ViewModeType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6CssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconType;

/**
 * Example page of tempus-dominus picker usage
 *
 */
public class TempusDominusPickerPage extends BasePage {
    private static final long serialVersionUID = 1L;
    private Form<Object> form;
    private NotificationPanel feedback;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public TempusDominusPickerPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        LocalDate max = LocalDate.now();
        LocalDate min = max.minusWeeks(1);

        form = new Form<>("form");
        feedback = new NotificationPanel("feedback");
        add(form.setOutputMarkupId(true));

        DropDownChoice<String> languages = new DropDownChoice<String>("languages"
                , Model.of(Session.get().getLocale().getLanguage())
                , List.of(Locale.getISOLanguages())
                , new LambdaChoiceRenderer<String>(
                        lng -> Locale.forLanguageTag(lng).getDisplayLanguage(Locale.ENGLISH)
                        , lng -> lng));
        DropDownChoice<String> countries = new DropDownChoice<String>("countries"
                , Model.of(Session.get().getLocale().getCountry())
                , List.of(Locale.getISOCountries())
                , new LambdaChoiceRenderer<String>(
                        country -> Locale.forLanguageTag("en-" + country).getDisplayCountry(Locale.ENGLISH)
                        , country -> country));
        form.add(feedback.hideAfter(Duration.ofSeconds(10)).setOutputMarkupId(true)
                , languages.setRequired(true)
                , countries);

        // java.util.Date
        { // scope
            TempusDominusConfig simpleConfig = new TempusDominusConfig()
                    .withRestrictions(cfg -> cfg
                            .withMinDate(Date.from(min.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                            .withMaxDate(Date.from(max.plusWeeks(2).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    );
            createBlock(
                new DateTextField("simple", Model.of((Date)null), simpleConfig.getJavaFormat()),
                        """
                        new TempusDominusConfig()
                            .withRestrictions(cfg -> cfg
                                .withMinDate(Date.from(min.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                .withMaxDate(Date.from(max.plusWeeks(2).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                            );""",
                simpleConfig
            );
        }
        { // scope
            TempusDominusConfig birthDayConfig = new TempusDominusConfig()
                    .withDisplay(cfg -> cfg
                            .withViewMode(ViewModeType.YEARS)
                            .withComponent(ComponentType.CLOCK, false)
                    )
                    .withLocalization(cfg -> cfg
                            .withJavaFormat("dd/MM/yyyy")
                    )
                    .withRestrictions(cfg -> cfg
                            .withMaxDate(Date.from(max.minusYears(6).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    );
            createBlock(
                new DateTextField("birthday", Model.of((Date)null), birthDayConfig.getJavaFormat()),
                        """
                        new TempusDominusConfig()
                            .withDisplay(cfg -> cfg
                                .withViewMode(ViewModeType.YEARS)
                                .withComponent(ComponentType.CLOCK, false)
                            )
                            .withLocalization(cfg -> cfg
                                .withJavaFormat("dd/MM/yyyy")
                            )
                            .withRestrictions(cfg -> cfg
                                .withMaxDate(Date.from(max.minusYears(6).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                            );""",
                birthDayConfig
            );
        }
        { // scope
            TempusDominusConfig iconsConfig = new TempusDominusConfig()
                .withDisplay(cfg -> cfg
                        .withButton(ButtonType.TODAY, true)
                        .withButton(ButtonType.CLEAR, true)
                        .withButton(ButtonType.CLOSE, true)
                        .withIcons(icons -> icons
                                .withDateIcon(FontAwesome6IconType.calendar_days_r)
                                .withTimeIcon(FontAwesome6IconType.clock_s)
                                .withUpIcon(FontAwesome6IconType.arrow_up_s)
                                .withDownIcon(FontAwesome6IconType.arrow_down_s)
                                .withPreviousIcon(FontAwesome6IconType.arrow_left_s)
                                .withNextIcon(FontAwesome6IconType.arrow_right_s)
                                .withTodayIcon(FontAwesome6IconType.calendar_check_s)
                                .withClearIcon(FontAwesome6IconType.eraser_s)
                                .withCloseIcon(FontAwesome6IconType.xmark_s)
                        )
                        .withComponent(ComponentType.SECONDS, true)
                )
                .withLocalization(cfg -> cfg
                        .withJavaFormat("dd/MM/yyyy HH:mm:ss")
                );
            createBlock(
                new DateTextField("icons", Model.of((Date)null), iconsConfig.getJavaFormat()),
                        """
                        new TempusDominusConfig()
                            .withDisplay(cfg -> cfg
                                .withButton(ButtonType.TODAY, true)
                                .withButton(ButtonType.CLEAR, true)
                                .withButton(ButtonType.CLOSE, true)
                                .withIcons(icons -> icons
                                    .withDateIcon(FontAwesome6IconType.calendar_days_r)
                                    .withTimeIcon(FontAwesome6IconType.clock_s)
                                    .withUpIcon(FontAwesome6IconType.arrow_up_s)
                                    .withDownIcon(FontAwesome6IconType.arrow_down_s)
                                    .withPreviousIcon(FontAwesome6IconType.arrow_left_s)
                                    .withNextIcon(FontAwesome6IconType.arrow_right_s)
                                    .withTodayIcon(FontAwesome6IconType.calendar_check_s)
                                    .withClearIcon(FontAwesome6IconType.eraser_s)
                                    .withCloseIcon(FontAwesome6IconType.xmark_s)
                                )
                            .withComponent(ComponentType.SECONDS, true)
                        )
                        .withLocalization(cfg -> cfg
                            .withJavaFormat("dd/MM/yyyy HH:mm:ss")
                        );""",
                iconsConfig
            );
        }
        // java.time.*
        { // scope
            TempusDominusConfig localConfig = new TempusDominusConfig()
                .withClass(LocalDateTime.class)
                .withRestrictions(cfg -> cfg
                        .withMinDate(min.atStartOfDay())
                        .withMaxDate(max.atStartOfDay())
                )
                .withLocalization(cfg -> cfg
                        .withJavaFormat("dd/MM/yyyy HH:mm:ss")
                );
            createBlock(
                new LocalDateTimeTextField("localsimple", Model.of((LocalDateTime)null), localConfig.getJavaFormat()),
                        """
                        new TempusDominusConfig()
                            .withClass(LocalDateTime.class)
                            .withRestrictions(cfg -> cfg
                                .withMinDate(min.atStartOfDay())
                                .withMaxDate(max.atStartOfDay())
                            )
                            .withLocalization(cfg -> cfg
                                .withJavaFormat("dd/MM/yyyy HH:mm:ss")
                            );""",
                localConfig
            );
        }
        { // scope
            TempusDominusConfig localBirthConfig = new TempusDominusConfig()
                .withClass(LocalDate.class)
                .withDisplay(cfg -> cfg
                        .withViewMode(ViewModeType.YEARS)
                )
                .withRestrictions(cfg -> cfg
                        .withMaxDate(max.minusYears(6))
                );
            createBlock(
                new LocalDateTextField("localbirthday", Model.of((LocalDate)null), localBirthConfig.getJavaFormat()),
                        """
                        new TempusDominusConfig()
                            .withClass(LocalDate.class)
                            .withDisplay(cfg -> cfg
                                .withViewMode(ViewModeType.YEARS)
                            )
                            .withRestrictions(cfg -> cfg
                                .withMaxDate(max.minusYears(6))
                            );""",
                localBirthConfig
            );
        }
        { // scope
            TempusDominusConfig timeConfig = new TempusDominusConfig()
                .withClass(LocalTime.class);
            createBlock(
                new LocalTimeTextField("localtime", Model.of((LocalTime)null), timeConfig.getJavaFormat()),
                        """
                        new TempusDominusConfig()
                            .withClass(LocalTime.class);""",
                timeConfig
            );
        }
        { // scope
            TempusDominusConfig iconConfig = new TempusDominusConfig()
                .withClass(LocalDateTime.class)
                .withRestrictions(cfg -> cfg
                        .withDayOfWeekDisabled(0)
                        .withDayOfWeekDisabled(6)
                );
            FormComponent<?> iconInput = new AbstractTempusDominusWithIcon<>("icon", Model.of((LocalDateTime)null), iconConfig) {
                private static final long serialVersionUID = 1L;

                @Override
                protected FormComponent<LocalDateTime> newInput(String wicketId, String dateFormat) {
                    LocalDateTimeTextField input = new LocalDateTimeTextField(wicketId, dateFormat);
                    addStatus(this, input);

                    return input;
                }
            };
            createBlock(
                iconInput,
                """
                new TempusDominusConfig()
                    .withClass(LocalDateTime.class)
                    .withRestrictions(cfg -> cfg
                        .withDayOfWeekDisabled(0)
                        .withDayOfWeekDisabled(6)
                    );"""
            );
        }
        form.add(new AjaxButton("submit") {
            protected void onSubmit(AjaxRequestTarget target) {
                String tag = languages.getModelObject();
                final String country = countries.getModelObject();
                if (!Strings.isEmpty(country)) {
                    tag += "-" + country;
                }
                Locale loc = Locale.forLanguageTag(tag);
                form.info("'" + loc + "' has been set");
                Session.get().setLocale(loc);
                target.add(form);
            }

            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }
        });
    }

    private void addStatus(FormComponent<?> input, FormComponent<?> innerInput) {
        final IConverter converter = new IConverter() {
            public String convertToString(Object value, Locale locale) {
                return TempusDominusConfig.formatDateISO(value);
            }

            @Override
            public Object convertToObject(String value, Locale locale) throws ConversionException {
                return null;
            }
        };

        Label status = new Label(input.getId() + "-status", input.getModel()) {
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return converter;
            }
        };
        FormComponent<?> dateInput = innerInput == null ? input : innerInput;
        dateInput.setOutputMarkupId(true);
        dateInput.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                status.setDefaultModelObject(TempusDominusConfig.formatDateISO(dateInput.getModelObject()));
                target.add(status);
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                target.add(feedback);
            }
        });
        form.add(status.setOutputMarkupId(true));
    }

    private void addCode(String id, String code) {
        form.add(new Code(id + "-java-code", Model.of(code)));
    }

    private void createBlock(FormComponent<?> input, String code) {
        addCode(input.getId(), code);
        form.add(input);
    }

    private void createBlock(FormComponent<?> input, String code, TempusDominusConfig cfg) {
        createBlock(input, code);
        addStatus(input, null);
        input.add(new TempusDominusBehavior(cfg));
    }

    @Override
    protected boolean hasNavigation() {
        return false; // we will use internal one
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(TempusDominusPickerPage.class, "css/layout.css")));
        response.render(CssHeaderItem.forReference(FontAwesome6CssReference.instance()));
    }
}
