package model;

import java.util.HashMap;
import java.util.Map;

public class Discipline {

	private int absenses;
	private String name;
	private String code;
	private String classroomCode;
	private int classRoomId;
	private int periodId;
	private int presences;
	private int courseId;
	private String teacherName;
	private int teacherId;
	private int frequency;
	private String state;
	private String period;
	private int grade;
	
	// Cria HashMap com os estados da disciplina
	private static final Map<String, String> DISCIPLINE_STATE;
	static {
		DISCIPLINE_STATE = new HashMap<String, String>();
		DISCIPLINE_STATE.put("approved", "Aprovado");
		DISCIPLINE_STATE.put("attending", "Em Curso");
		DISCIPLINE_STATE.put("not-attended", "Não cursado");
		DISCIPLINE_STATE.put("dismissed", "Dispensado");
	}

	public int getAbsenses() {
		return absenses;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getClassroomCode() {
		return classroomCode;
	}

	public int getClassRoomId() {
		return classRoomId;
	}

	public int getPeriodId() {
		return periodId;
	}

	public int getPresences() {
		return presences;
	}

	public int getCourseId() {
		return courseId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public int getFrequency() {
		return frequency;
	}

	public String getState() {
		return DISCIPLINE_STATE.get(state);
	}

	public String getPeriod() {
		return period;
	}

	public int getGrade() {
		return grade;
	}
}
