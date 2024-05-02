import axios, {AxiosResponse} from 'axios';

export function registerEquipment(): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/equipment/register',
        method: 'POST',
    })
}

export function removeEquipmentPosById(id: number): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/equipment/removeEquipmentPosById',
        method: 'DELETE',
        params: {id}
    })
}

export function getAllEquipment(): Promise<AxiosResponse<Result<Equipment[]>, any>> {
    return axios({
        url: '/equipment/getAllEquipment',
        method: 'GET'
    })
}

export function getEquipmentById(id: number): Promise<AxiosResponse<Result<Equipment>, any>> {
    return axios({
        url: '/equipment/getEquipmentById',
        method: 'GET',
        params: {id}
    })
}

export function getEquipmentByIdArray(id: number[]): Promise<AxiosResponse<Result<Equipment[]>, any>> {
    return axios({
        url: '/equipment/getEquipmentByIdArray',
        method: 'GET',
        params: {id}
    })
}

export function updateEquipmentAnotherNameById(id: number, anotherName: string): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/equipment/updateEquipmentAnotherNameById',
        method: 'PUT',
        params: {id, anotherName}
    })
}

export function updateEquipmentTimeById(id: number): Promise<AxiosResponse<Result<undefined>, any>> {

    return axios({
        url: '/equipment/updateEquipmentTimeById',
        method: 'PUT',
        params: {id}
    })
}

export function removeDataTypeById(id: number): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: "/dataType/removeDataTypeById",
        method: "DELETE",
        data: {id}
    });
}

export function getAllDataType(): Promise<AxiosResponse<Result<DataType[]>, any>> {
    return axios({
        url: "/dataType/getAllDataType",
        method: "GET"
    });
}

export function getDataTypeById(id: number): Promise<AxiosResponse<Result<DataType>, any>> {
    return axios({
        url: "/dataType/getDataTypeById",
        method: "GET",
        params: {id}
    });
}


export function getDataTypeByIdArray(id: number): Promise<AxiosResponse<Result<DataType[]>, any>> {
    return axios({
        url: "/dataType/getDataTypeByIdArray",
        method: "GET",
        params: {id}
    });
}

export function updateDataTypeAnotherName(id: number, anotherName: string): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: "/dataType/updateDataTypeAnotherName",
        method: "PUT",
        params: {id, anotherName}
    })
}

export function registerData(data: Data): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/data/addData',
        method: 'POST',
        params: {data}
    })
}

export function getDataById(id: number): Promise<AxiosResponse<Result<Data>, any>> {
    return axios({
        url: '/data/getDataById',
        method: 'GET',
        params: {id}
    })
}

export function getAllData(): Promise<AxiosResponse<Result<Data[]>, any>> {
    return axios({
        url: '/data/getAllData',
        method: 'GET'
    })
}

export function getData(
    equipmentId: number,
    dataTypeId: number,
    start: number,
    end: number
): Promise<AxiosResponse<Data[], any>> {
    return axios({
        url: '/data/getData',
        method: 'GET',
        params: {equipmentId, dataTypeId, start, end}
    })
}

export function getDataMapFromEquipmentIdArray(
    equipmentIdArray: number[],
    dataTypeId: number,
    start: number,
    end: number
): Promise<AxiosResponse<Map<number, Data>, any>> {
    return axios({
        url: '/data/getDataMapFromEquipmentIdArray',
        method: 'GET',
        params: {equipmentIdArray, dataTypeId, start, end}
    })
}

export function getDataMapFromDataTypeIdArray(
    equipmentId: number,
    dataTypeIdArray: number[],
    start: number,
    end: number
): Promise<AxiosResponse<Map<number, Data>, any>> {
    return axios({
        url: '/data/getDataMapFromDataTypeIdArray',
        method: 'GET',
        params: {equipmentId, dataTypeIdArray, start, end}
    })
}


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
    anotherName: string
}

export interface Equipment {
    id: number
    anotherName: string
    longitude: number
    latitude: number
    upTime: string
}
