CREATE TABLE achievements (
    id INTEGER NOT NULL,
    title TEXT NOT NULL DEFAULT '',
    achievement_type TEXT NOT NULL DEFAULT '',
    actual_score INTEGER NOT NULL DEFAULT 0,
    required_score INTEGER NOT NULL DEFAULT 1000,
    PRIMARY KEY(id)
);

CREATE TABLE healthadvices (
    id INTEGER NOT NULL,
    category TEXT NOT NULL DEFAULT '',
    description TEXT NOT NULL DEFAULT '',
    PRIMARY KEY(id)
);

INSERT INTO achievements (id, title, achievement_type, required_score)
VALUES (0, 'Caminar 1000 pasos', 'steps', 1000),
       (1, 'Caminar 2000 pasos', 'steps', 2000),
       (2, 'Caminar 300 kilómetros', 'kilometers', 300),
       (3, 'Quemar 700 calorías', 'calories', 700),
       (4, 'Usar la app durante 30 días consecutivos', 'days', 30);

INSERT INTO healthadvices (id, category, description)
VALUES (0, 'Alimentación', 'Come frutas y verduras de colores variados cada día.'),
       (1, 'Alimentación', 'Elige cereales integrales en lugar de refinados.'),
       (2, 'Alimentación', 'Limita el consumo de carnes rojas y procesadas.'),
       (3, 'Alimentación', 'Opta por grasas saludables como el aceite de oliva.'),
       (4, 'Alimentación', 'Bebe suficiente agua durante el día.'),
       (5, 'Actividad física', 'Realiza al menos 30 minutos de ejercicio moderado al día.'),
       (6, 'Actividad física', 'Incorpora actividades que te gusten y puedas mantener a largo plazo.'),
       (7, 'Actividad física', 'Muévete con frecuencia, evita el sedentarismo.'),
       (8, 'Actividad física', 'Sube las escaleras en lugar del ascensor.'),
       (9, 'Actividad física', 'Camina o anda en bicicleta siempre que puedas.'),       
       (10, 'Bienestar general', 'Duerme de 7 a 8 horas cada noche.'),
       (11, 'Bienestar general', 'Controla el estrés con técnicas como la meditación o el yoga.'),
       (12, 'Bienestar general', 'Pasa tiempo al aire libre y disfruta de la naturaleza.'),
       (13, 'Bienestar general', 'Cultiva relaciones sociales sanas y positivas.'),
       (14, 'Bienestar general', 'Realiza actividades que te brinden alegría y satisfacción.'),
       (15, 'Hábitos saludables', 'Evita fumar y el consumo excesivo de alcohol.'),
       (16, 'Hábitos saludables', 'Realízate chequeos médicos regulares.'),
       (17, 'Hábitos saludables', 'Protege tu piel del sol con protector solar.'),
       (18, 'Hábitos saludables', 'Mantén una buena higiene personal.'),
       (19, 'Hábitos saludables', 'Cuida tu salud mental y busca ayuda si la necesitas.');
