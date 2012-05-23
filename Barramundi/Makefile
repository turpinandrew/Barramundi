.SUFFIXES:	.java .class

DEFS =     # -DVERBOSE_OUTPUT # -DCOMMAPI_PRESENT 

MANIFEST = META-INF/MANIFEST.MF

CLI_MANIFEST = META-INF/CLI_MANIFEST.MF

GUI_CLASSES = Simulator.class Control.class

CLI_CLASSES = CLISimulator.class CLIControl.class

COMMON_CLASSES = AbstractPatientModifier.class AbstractTestProcedure.class BarGraphDrawer.class BarGraphPanel.class Database.class DatabaseException.class DatabaseRecord.class GaussianNoise.class Interface.class LineGraphDrawer.class LineGraphPanel.class NoNoise.class Pair.class Patient.class PatientData.class PatientDataRecord.class PatientResult.class PatientModifierException.class PatientResults.class PatientSetRecord.class PatientSets.class QuickSort.class Seen.class SortableObject.class StaticModifier.class Stimuli.class StimulusRecord.class TestProcedureException.class TestProcedureRecord.class TestProcedures.class TPGeneralStaircase.class SeenFactory.class HFA.class TestHFA.class TPZest.class TPSEMU.class TPSEMU_KS.class TPEMU.class TPFullThreshold242.class

#%.java: %.jc
#	gcc -E -P $< > $*.java

%.class: %.jc
	gcc -x c -E -P $(DEFS) $< | awk '/#pragma/{next}{print}' > $*.java
	( cd .. ; javac Barramundi/$*.java )

%.class: %.java
	( cd .. ; javac Barramundi/$< )

all: gui cli

gui: $(GUI_CLASSES) $(COMMON_CLASSES) ../$(MANIFEST)
	( cd .. ; jar cfm Barramundi.jar $(MANIFEST) Barramundi )

cli: $(CLI_CLASSES) $(COMMON_CLASSES) ../$(MANIFEST)
	( cd .. ; jar cfm cliBarramundi.jar $(CLI_MANIFEST) Barramundi )

run:
	java -jar Barramundi.jar

commapi: Makefile
	rm `ls *.jc | sed 's/jc/class/'`

clean:
	rm *.class
