import store from '../../store'
import router from '../../router'
import user from "./user";

const project= {
  state: {
    projectId: '',
    projectCode: '',
    projectName: '',
    projectDesc: ''
  },
  mutations: {
    SET_PROJECT: (state, project) => {
      state.projectId = project.projectId;
      state.projectCode = project.projectCode;
      state.projectName = project.projectName;
      state.projectDesc = project.projectDesc;
    },

    SET_PROJECTID: (state, projectId) => {
      state.projectId = projectId;
    },

    SET_PROJECTCODE:(state,projectCode)=>{
      state.projectCode = projectCode;
    }
  },
  // actions: {
  //
  // }
  }

export default project
