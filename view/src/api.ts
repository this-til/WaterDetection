import axios, {AxiosResponse} from 'axios';
import {ElMessage} from "element-plus";
import {AxiosInstance} from "axios/index";

const apiString = () => {
    return "/api"
};
var api: AxiosInstance;
addToken('')
export var _token: string = '';

export function addToken(token: string) {
    _token = token
    api = axios.create({
        headers: {
            'Content-Type': 'application/json',
            'token': token
        },
        baseURL: apiString(),
    });
    api.interceptors.response.use(function (response) {
        // 对响应数据做点什么
        // 例如，你可以在这里处理错误代码

        switch (response.data.resultType) {
            case 'FAIL':

                ElMessage({
                    message: '执行失败:' + response.data.message,
                    type: 'warning',
                })
                break
            case 'ERROR':
                ElMessage({
                    message: '执行错误:' + response.data.message,
                    type: 'error',
                })
                break
        }


        if (response.data.errorCode) {
            return Promise.reject(response.data.errorMessage);
        }
        return response;
    }, function (error) {
        ElMessage({
            message: '响应错误:' + error.message,
            type: 'error',
        })
        return Promise.reject(error);
    });
}


export const LoginApi = {
    login: (username: string, password: string): Promise<AxiosResponse<Result<string>>> => api.post('/login', null, {
        params: {
            username,
            password
        }
    }),
}


export const CommandApi = {
    registerCommand: (ruleId: number, actuatorId: number, commandTrigger: number): Promise<AxiosResponse<Result<void>>> => api.post('/command/registerCommand', {
        params: {
            ruleId,
            actuatorId,
            commandTrigger
        }
    }),
    removeCommandById: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/command/removeCommandById', {params: {id}}),
    updateCommandById: (id: number, command: Command): Promise<AxiosResponse<Result<void>>> => axios.put(`/command/updateCommandById`, command, {params: {id}}),
    getCommandById: (id: number): Promise<AxiosResponse<Result<Command>>> => api.get('/command/getCommandById', {params: {id}}),
    getCommandByIdArray: (id: number[]): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getCommandByIdArray', {params: {id: id.join(',')}}),
    getCommandByActuatorId: (actuatorId: number): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getCommandByActuatorId', {params: {actuatorId}}),
    getCommandByRuleId: (ruleId: number): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getCommandByRuleId', {params: {ruleId}}),
    getAllCommands: (): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getAllCommands'),
};

export const DataApi = {
    addData: (data: Data): Promise<AxiosResponse<Result<void>>> => api.post('/data/addData', data),
    addDataSimple: (equipmentId: number, dataTypeId: number, time: Date | null, value: number): Promise<AxiosResponse<Result<void>>> => api.post('/data/addDataSimple', {
        equipmentId,
        dataTypeId,
        time: time?.getTime(),
        value
    }),
    addDataList: (dataList: Data[]): Promise<AxiosResponse<Result<void>>> => api.post('/data/addDataList', dataList),
    getDataById: (id: number): Promise<AxiosResponse<Result<Data>>> => api.get('/data/getDataById', {params: {id}}),
    getAllData: (): Promise<AxiosResponse<Result<Data[]>>> => api.get('/data/getAllData'),
    getData: (equipmentId: number, dataTypeId: number, start: Date, end: Date): Promise<AxiosResponse<Result<Data[]>>> => api.get('/data/getData', {
        params: {
            equipmentId,
            dataTypeId,
            start: start.getTime(),
            end: end.getTime(),
        }
    }),
    getDataToDataSheet: (dataTypeId: number, equipmentIdArray: number[], timeStep: number, startTime: Date, endTime: Date): Promise<AxiosResponse<Result<DataSheet>>> => api.get('/data/getDataToDataSheet', {
        params: {
            dataTypeId,
            equipmentIdArray: equipmentIdArray.join(','),
            timeStep,
            startTime: startTime.getTime(),
            endTime: endTime.getTime(),
        }
    }),
};

export const DataTypeApi = {
    registerDataType: (name: string): Promise<AxiosResponse<Result<void>>> => api.post('/dataType/register', null, {params: {name}}),
    removeDataTypeById: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/dataType/removeDataTypeById', {params: {id}}),
    getDataTypeById: (id: number): Promise<AxiosResponse<Result<DataType>>> => api.get('/dataType/getDataTypeById', {params: {id}}),
    getDataTypeByName: (name: string): Promise<AxiosResponse<Result<DataType>>> => api.get('/dataType/getDataTypeByName', {params: {name}}),
    getAllDataType: (): Promise<AxiosResponse<Result<DataType[]>>> => api.get('/dataType/getAllDataType'),
    getDataTypeByIdArray: (idArray: number[]): Promise<AxiosResponse<Result<DataType[]>>> => api.get('/dataType/getDataTypeByIdArray', {params: {id: idArray.join(',')}}),
    getDataTypeByNameArray: (nameArray: string[]): Promise<AxiosResponse<Result<DataType[]>>> => api.get('/dataType/getDataTypeByNameArray', {params: {name: nameArray.join(',')}}),
};

export const EquipmentApi = {
    registerEquipment: (name: string): Promise<AxiosResponse<Result<void>>> => api.post('/equipment/registerEquipment', null, {params: {name}}),
    removeEquipmentPosById: (equipmentId: number): Promise<AxiosResponse<Result<void>>> => api.delete('/equipment/removeEquipmentPosById', {params: {equipmentId}}),
    updateEquipmentAnotherNameById: (id: number, anotherName: string): Promise<AxiosResponse<Result<void>>> => api.put('/equipment/updateEquipmentAnotherNameById', null, {
        params: {
            id,
            anotherName
        }
    }),
    updateEquipmentTimeById: (id: number): Promise<AxiosResponse<Result<void>>> => api.put('/equipment/updateEquipmentTimeById', null, {params: {id}}),
    updateEquipmentPosById: (id: number, longitude: number, latitude: number): Promise<AxiosResponse<Result<void>>> => api.put('equipment/updateEquipmentPosById', null, {
        params: {
            id,
            latitude,
            longitude
        }
    }),
    updateEquipmentFencePosById: (id: number, electronicFence: boolean, longitude: number, latitude: number, range: number): Promise<AxiosResponse<Result<void>>> => api.put('equipment/updateEquipmentFencePosById', null, {
        params: {
            id,
            latitude,
            longitude,
            electronicFence,
            range
        }
    }),
    getEquipmentById: (id: number): Promise<AxiosResponse<Result<Equipment>>> => api.get('/equipment/getEquipmentById', {params: {id}}),
    getEquipmentByName: (name: string): Promise<AxiosResponse<Result<Equipment>>> => api.get('/equipment/getEquipmentByName', {params: {name}}),
    getAllEquipment: (): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getAllEquipment'),
    getAllOnlineEquipment: (): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getAllOnlineEquipment'),
    getAllOnlineEquipmentId: (): Promise<AxiosResponse<Result<number[]>>> => api.get('/equipment/getAllOnlineEquipmentId'),
    getAllOnlineEquipmentRunTime: () :  Promise<AxiosResponse<Result<EquipmentRunTime[]>>> => api.get('/equipment/getAllOnlineEquipmentRunTime'),
    getOnlineEquipment: (id: number): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getOnlineEquipment', {params: {id}}),
    getEquipmentByIdArray: (idArray: number[]): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getEquipmentByIdArray', {params: {id: idArray.join(',')}}),
    getEquipmentByNameArray: (nameArray: string[]): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getEquipmentByNameArray', {params: {name: nameArray.join(',')}}),
};

export const RuleApi = {
    registerRule: (rule: Rule): Promise<AxiosResponse<Result<void>>> => api.post('/rule/registerRule', rule),
    deleteByID: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/rule/deleteByID', {params: {id}}),
    updateById: (id: number, rule: Rule): Promise<AxiosResponse<Result<void>>> => api.put('/rule/updateById', rule, {params: {id}}),
    getRuleById: (id: number): Promise<AxiosResponse<Result<Rule>>> => api.get('/rule/getRuleById', {params: {id}}),
    getRuleByEquipmentId: (equipmentId: number): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getRuleByEquipmentId', {params: {equipmentId}}),
    getRuleByDataTypeId: (dataTypeId: number): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getRuleByDataTypeId', {params: {dataTypeId}}),
    getRuleByEquipmentIdArray: (idArray: number[]): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getRuleByEquipmentIdArray', {params: {id: idArray.join(',')}}),
    getAllRule: (): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getAllRule'),
};

export const ActuatorApi = {
    registerActuator: (name: string): Promise<AxiosResponse<Result<void>>> => api.post('/actuator/registerActuator', {name}),
    removeActuatorById: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/actuator/removeActuatorById', {params: {id}}),
    updateActuatorByEquipmentId: (equipmentId: number, embeddedId: number, open: boolean): Promise<AxiosResponse<Result<void>>> => api.put('/actuator/updateActuatorByEquipmentId', null, {
        params: {
            equipmentId,
            embeddedId,
            open
        }
    }),
    getActuatorById: (id: number): Promise<AxiosResponse<Result<Actuator>>> => api.get('/actuator/getActuatorById', {params: {id}}),
    getActuatorByName: (name: string): Promise<AxiosResponse<Result<Actuator>>> => api.get('/actuator/getActuatorByName', {params: {name}}),
    getAllActuator: (): Promise<AxiosResponse<Result<Actuator[]>>> => api.get('/actuator/getAllActuator'),
    getActuatorByIdArray: (idArray: number[]): Promise<AxiosResponse<Result<Actuator[]>>> => api.get('/actuator/getActuatorByIdArray', {params: {id: idArray.join(',')}}),
    getActuatorByNameArray: (nameArray: string[]): Promise<AxiosResponse<Result<Actuator[]>>> => api.get('/actuator/getActuatorByNameArray', {params: {name: nameArray.join(',')}}),
};

export enum ResultType {
    SUCCESSFUL = "SUCCESSFUL",
    FAIL = "FAIL",
    ERROR = "ERROR"
}

export function getResultTypeFromString(name: string): ResultType | undefined {
    return ResultType[name as keyof typeof ResultType];
}

export interface Result<T = any> {
    resultType: string //请去响应一个ResultType
    message: string
    data: T
}

export interface Data {
    id: number;
    detectionId: number;
    dataTypeId: number;
    time: string; // 请求响应一个时间戳 类似 "2024-04-28T13:10:36.607+00:00" 这边不做转换
    value: number; // float 在 TypeScript 中对应 number 类型
}

export interface DataType {
    id: number
    name: string
}

export interface Actuator {
    id: number
    name: string
}

export interface Equipment {
    id: number
    name: string
    upTime: number
    longitude: number
    latitude: number

    electronicFence: boolean
    fenceLongitude: number
    fenceLatitude: number
    fenceRange: number
}

export interface DataSheet {
    dataType: DataType
    timeStep: number
    startTime: number
    endTime: number

    equipmentList: Equipment[]
    timestampList: number[]

    //equipment -> timestamp
    value: number[][]
}

export interface Command {
    id: number
    ruleId: number
    actuatorId: number
    commandTrigger: number
}

export interface Rule {
    id: number
    datatypeId: number
    equipmentId: number

    exceptionUpper: number
    warnUpper: number
    warnLower: number
    exceptionLower: number

    warnSendMessage: boolean
    exceptionSendMessage: boolean
}

export interface ActuatorRuntime {

    activated: boolean
    embeddedId: number
    actuator: Actuator
}

export interface DataTypeRunTime {
    value: number
    dataState: number
    embeddedId: number
    dataType: DataType
    rule: Rule
}

export interface EquipmentRunTime {

    equipment: Equipment
    actuatorRuntimeList: ActuatorRuntime[];
    dataTypeRuntimeList: DataTypeRunTime[];

}
